package com.noname.duyuru.app.service;

import com.noname.duyuru.app.jpa.models.*;
import com.noname.duyuru.app.jpa.repositories.AnnouncementRepository;
import com.noname.duyuru.app.jpa.repositories.SubscriptionRepository;
import com.noname.duyuru.app.jpa.repositories.TopicRepository;
import com.noname.duyuru.app.mvc.message.IViewMessage;
import com.noname.duyuru.app.mvc.message.SuccessMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.client.ResourceAccessException;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@Service
public class AnnouncementService {
	private static final Logger LOGGER = LogManager.getLogger(AnnouncementService.class);

	private final SubscriptionRepository subscriptionRepository;
	private final AnnouncementRepository announcementRepository;
	private final MessageSender messageSender;
	private final TopicRepository topicRepository;

	public AnnouncementService(SubscriptionRepository subscriptionRepository, AnnouncementRepository announcementRepository, MessageSender messageSender, TopicRepository topicRepository) {
		this.subscriptionRepository = subscriptionRepository;
		this.announcementRepository = announcementRepository;
		this.messageSender = messageSender;
		this.topicRepository = topicRepository;
	}

	public Page<Announcement> getAnnouncements(Pageable page) {
		return announcementRepository.findAll(page);
	}

	@Transactional(readOnly = true)
	public Page<User> getUsersWithSubscriptions(Pageable pageable) {
		final Page<User> usersWithSubscriptions = subscriptionRepository.getUsersWithSubscriptions(pageable);
		/*for (User u : usersWithSubscriptions) {
			u.getSubscriptions().size();
		}*/
		return usersWithSubscriptions;
	}

	public void checkNewAnnouncements() {
		final List<Topic> topics = subscriptionRepository.findDistinctTopics();
		for (final Topic topic : topics) {
			LOGGER.debug("Checking {}", topic.getId());
			try {
				/*
				switch (topic.getType()) {
					case CENG_CLASS:
						classCheck(topic);
						break;
					case MF:
						mfCheck(topic);
						break;
					case CENG:
						//logger.error("department: "+topic.getId()+" exist in topic table");
					//TODO
					departmentCheck(topic);
					break;
				}
				 */
				checkAnnouncements(topic);
			} catch (final ResourceAccessException | IOException e) {
				LOGGER.error("could not access topic: {}({})", topic.getId(), e.getClass().getName());
			}
		}
	}

	public IViewMessage clearAnnouncements() {
		StringBuilder sb=new StringBuilder();
		for (Topic topic:topicRepository.getByType(TopicType.CENG_CLASS)){
			List<Announcement> savedAnnouncements=announcementRepository.getAllByTopic(topic);

			try {
				final Document doc = Jsoup.connect(topic.getAnnouncementLink()).get();
				final Elements links = doc.select("#ContentPlaceHolder1_TreeView1>div a");
				for (Element link : links) {
					//TODO Announcement builder from html element
					final Announcement announcement = buildAnnouncement(topic, link);
					savedAnnouncements.remove(announcement);
				}
				if (savedAnnouncements.size() > 0) {
					sb.append(topic.getId());
					sb.append(": ");
					for (Announcement announcement : savedAnnouncements) {
						sb.append(announcement.getTitle());
						sb.append(", ");
					}
					announcementRepository.deleteAll(savedAnnouncements);
				}
			} catch (IOException e) {
				LOGGER.error(e);
			}

		}
		LOGGER.info("Cleared announcements; {}", sb.toString());
		return new SuccessMessage(sb.toString());
	}

	@Async
	void processLinks(final Topic topic, final Elements links) {
		for (final Element link : links) {
			try {
				//TODO Announcement builder from html element
				final Announcement announcement = buildAnnouncement(topic, link);
				announcement.setDate(new Date());

				final AnnouncementKey key = announcement.getId();
				if (!announcementRepository.existsById(key)) {
					announcementRepository.save(announcement);
					notifySubscribers(announcement);
				}
			} catch (Exception e) {
				LOGGER.error(e);
			}
		}
	}

	private void checkAnnouncements(final Topic topic) throws IOException {
		final Document doc = Jsoup.connect(topic.getAnnouncementLink()).get();
		final Elements links = doc.select(topic.getAnnSelector());
		LOGGER.debug("{} links are going to be checked with general method", links.size());
		processLinks(topic, links);
	}

	//TODO remove
	private void mfCheck(final Topic topic) throws IOException {
		final Document doc = Jsoup.connect(topic.getAnnouncementLink()).get();
		final Elements links = doc.select("#block-system-main tbody a");
		LOGGER.debug("{} links are going to be checked", links.size());
		processLinks(topic, links);
	}

	//TODO remove
	private void departmentCheck(final Topic topic) throws IOException {
		final Document doc = Jsoup.connect(topic.getAnnouncementLink()).get();
		final Elements rows = doc.select("#ContentPlaceHolder1_GridView1 tr");
		rows.last().empty();
		final Elements links = rows.select("a");
		LOGGER.debug("{} links are going to be checked", links.size());
		processLinks(topic, links);
	}

	//TODO remove
	private void classCheck(final Topic topic) throws IOException {
		final Document doc = Jsoup.connect(topic.getAnnouncementLink()).get();
		final Elements links = doc.select("div#ContentPlaceHolder1_TreeView1>div a");
		LOGGER.debug("{} links are going to be checked", links.size());
		processLinks(topic, links);
	}

	private Announcement buildAnnouncement(Topic topic, Element htmlElement) {
		final Announcement announcement = new Announcement();
		if (StringUtils.isEmpty(topic.getAnnTitleSelector())) {
			announcement.setTitle(htmlElement.html());
		} else {
			announcement.setTitle(htmlElement.selectFirst(topic.getAnnTitleSelector()).html());
		}
		announcement.setTopic(topic);
		if (StringUtils.isEmpty(topic.getAnnLinkSelector())) {
			announcement.setLink(htmlElement.attr("href"));
		} else {
			announcement.setLink(htmlElement.selectFirst(topic.getAnnLinkSelector()).attr("href"));
		}
		return announcement;
	}

	private void notifySubscribers(final Announcement announcement) {
		for (Subscription subscription : subscriptionRepository.findAllByTopic(announcement.getTopic())) {
			messageSender.sendMessage(subscription.getUser(), announcement.toString());
		}
	}
}
