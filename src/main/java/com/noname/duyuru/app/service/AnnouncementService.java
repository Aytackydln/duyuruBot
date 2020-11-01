package com.noname.duyuru.app.service;

import com.noname.duyuru.app.jpa.models.Announcement;
import com.noname.duyuru.app.jpa.models.Subscription;
import com.noname.duyuru.app.jpa.models.Topic;
import com.noname.duyuru.app.jpa.repositories.AnnouncementRepository;
import com.noname.duyuru.app.jpa.repositories.SubscriptionRepository;
import com.noname.duyuru.app.jpa.repositories.TopicRepository;
import com.noname.duyuru.app.mvc.message.IViewMessage;
import com.noname.duyuru.app.mvc.message.SuccessMessage;
import com.noname.duyuru.app.service.telegram.TelegramService;
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
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@Service
public class AnnouncementService {
	private static final Logger LOGGER = LogManager.getLogger(AnnouncementService.class);

	private final SubscriptionRepository subscriptionRepository;
	private final AnnouncementRepository announcementRepository;
	private final TelegramService telegramService;
	private final TopicRepository topicRepository;

	public AnnouncementService(SubscriptionRepository subscriptionRepository, AnnouncementRepository announcementRepository, TelegramService telegramService, TopicRepository topicRepository) {
		this.subscriptionRepository = subscriptionRepository;
		this.announcementRepository = announcementRepository;
		this.telegramService = telegramService;
		this.topicRepository = topicRepository;
	}

	public Page<Announcement> getAnnouncements(Pageable page) {
		return announcementRepository.findAll(page);
	}

	public IViewMessage clearAnnouncements() {
		StringBuilder sb = new StringBuilder();
		for (Topic topic : topicRepository.getByDepartmentIdNotNull()) {
			List<Announcement> savedAnnouncements = announcementRepository.getAllByTopic(topic);

			try {
				final Document doc = Jsoup.connect(topic.getAnnouncementLink()).get();
				final Elements links = doc.select(topic.getAnnSelector());
				for (Element link : links) {
					//TODO Announcement builder from html element
					final Announcement announcement = buildAnnouncement(topic, link);
					savedAnnouncements.remove(announcement);    //Saved announcements becomes list to delete
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
	void checkAnnouncements(final Topic topic) throws IOException {
		final Document doc = Jsoup.connect(topic.getAnnouncementLink()).get();
		final Elements links = doc.select(topic.getAnnSelector());
		LOGGER.debug("{} links are going to be checked with general method", links.size());
		processLinks(topic, links);
	}

	private void processLinks(final Topic topic, final Elements links) {
		LOGGER.debug("processing topic: {}", topic);
		for (final Element link : links) {
			try {
				//TODO Announcement builder from html element
				final Announcement announcement = buildAnnouncement(topic, link);
				announcement.setDate(new Date());

				if (!announcementRepository.existsById(announcement.getId())) {
					announcementRepository.save(announcement);
					notifySubscribers(announcement);
				}
			} catch (Exception e) {
				LOGGER.error(e);
			}
		}
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
			telegramService.sendMessage(subscription.getUser(), announcement.toString());
		}
	}
}
