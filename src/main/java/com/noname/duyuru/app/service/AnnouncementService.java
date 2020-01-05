package com.noname.duyuru.app.service;

import com.noname.duyuru.app.jpa.models.*;
import com.noname.duyuru.app.jpa.repositories.*;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.ResourceAccessException;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@Service
public class AnnouncementService {
	private static final Logger LOGGER = LogManager.getLogger(AnnouncementService.class);

	private final SubscriptionRepository subscriptionRepository;
	private final DepartmentRepository departmentRepository;
	private final AnnouncementRepository announcementRepository;
	private final MessageSender messageSender;
	private final TopicRepository topicRepository;

	public AnnouncementService(final SubscriptionRepository subscriptionRepository,
			final DepartmentRepository departmentRepository, final AnnouncementRepository announcementRepository,
			final MessageSender messageSender, final TopicRepository topicRepository) {
		this.subscriptionRepository = subscriptionRepository;
		this.departmentRepository = departmentRepository;
		this.announcementRepository = announcementRepository;
		this.messageSender = messageSender;
		this.topicRepository=topicRepository;
	}

	public Page<Announcement> getAnnouncements(Pageable page) {
		return announcementRepository.findAll(page);
	}

	@Transactional
	public Page<User> getUsersWithSubscriptions(Pageable pageable) {
		Page<User> list = subscriptionRepository.findDistinctUsers(pageable);
		for (User u : list)
			u.getSubscriptions().size();
		return list;
	}

	public void checkNewAnnouncements() {
		final List<Topic> topics = subscriptionRepository.findDistinctTopics();
		for (final Topic topic : topics) {
			LOGGER.debug("Checking " + topic.getId());
			try {
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
			} catch (final ResourceAccessException | IOException e) {
				LOGGER.error("could not access topic: " + topic.getId() + "(" + e.getClass().getName() + ")");
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
				e.printStackTrace();
			}

		}
		LOGGER.info("Cleared annoucements; "+sb.toString());
		return new SuccessMessage(sb.toString());
	}

	private void mfCheck(final Topic topic) throws IOException{
		final Document doc=Jsoup.connect(topic.getAnnouncementLink()).get();
		final Elements links=doc.select("#block-system-main tbody a");
		LOGGER.debug(links.size()+" links are going to be checked");
		processLinks(topic, links);
		;
	}

	//TODO remove
	private void departmentCheck(final Topic topic) throws IOException {
		final Document doc = Jsoup.connect(topic.getAnnouncementLink()).get();
		final Elements rows = doc.select("#ContentPlaceHolder1_GridView1 tr");
		rows.last().empty();
		final Elements links = rows.select("a");
		LOGGER.debug(links.size() + " links are going to be checked");
		processLinks(topic, links);
	}

	private void classCheck(final Topic topic) throws IOException{
		final Document doc=Jsoup.connect(topic.getAnnouncementLink()).get();
		final Elements links=doc.select("div#ContentPlaceHolder1_TreeView1>div a");
		LOGGER.debug(links.size()+" links are going to be checked");
		processLinks(topic, links);
		;
	}

	private void processLinks(final Topic topic, final Elements links) {
		for (final Element link : links) {
			try {
				//TODO Announcement builder from html element
				final Announcement announcement = buildAnnouncement(topic, link);
				announcement.setDate(new Date());

				final AnnouncementKey key = announcement.getId();
				if (!announcementRepository.existsById(key)) {
					announcementRepository.save(announcement);
					notifySubscribers(topic, announcement);
				}
			} catch (Exception e) {
				LOGGER.error(e);
			}
		}
	}

	private Announcement buildAnnouncement(Topic topic, Element htmlElement) {
		final Announcement announcement = new Announcement();
		announcement.setTitle(htmlElement.html());
		announcement.setTopic(topic);
		announcement.setLink(htmlElement.attr("href"));
		return announcement;
	}

	private void notifySubscribers(final Topic topic, final Announcement announcement) {
		final List<Subscription> subscriptions = subscriptionRepository.findAllByTopic(topic);
		for (final Subscription subscription : subscriptions) {
			messageSender.sendMessage(subscription.getUser(), announcement.toString());
		}
	}
}
