package aytackydln.duyuru.service;

import aytackydln.duyuru.jpa.models.SubscriptionEntity;
import aytackydln.duyuru.jpa.models.TopicEntity;
import aytackydln.duyuru.service.telegram.TelegramService;
import aytackydln.duyuru.jpa.models.AnnouncementEntity;
import aytackydln.duyuru.jpa.repository.AnnouncementRepository;
import aytackydln.duyuru.jpa.repository.SubscriptionRepository;
import aytackydln.duyuru.jpa.repository.TopicRepository;
import aytackydln.duyuru.mvc.message.SuccessMessage;
import aytackydln.duyuru.mvc.message.ViewMessage;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Log4j2
public class AnnouncementService {
	private final SubscriptionRepository subscriptionRepository;
	private final AnnouncementRepository announcementRepository;
	private final TelegramService telegramService;
	private final TopicRepository topicRepository;

	public Page<AnnouncementEntity> getAnnouncements(Pageable page) {
		return announcementRepository.findAll(page);
	}

	public ViewMessage clearAnnouncements() {
		List<AnnouncementEntity> clearedAnnouncementEntities = new ArrayList<>();
		for (TopicEntity topicEntity : topicRepository.getByDepartmentIdNotNull()) {
			clearedAnnouncementEntities.addAll(clearAnnouncements(topicEntity));
		}
		Stream<String> clearedAnnouncementStrings = clearedAnnouncementEntities.stream().map(AnnouncementEntity::toString);
		LOGGER.info("Cleared announcements; {}", clearedAnnouncementStrings);
		return new SuccessMessage(clearedAnnouncementStrings.toString());
	}

	private List<AnnouncementEntity> clearAnnouncements(TopicEntity topicEntity) {
		try {
			List<AnnouncementEntity> persistedAnnouncementEntities = announcementRepository.getAllByTopic(topicEntity);
			List<AnnouncementEntity> currentAnnouncementEntities = getCurrentAnnouncementsFromPage(topicEntity);
			persistedAnnouncementEntities.removeAll(currentAnnouncementEntities);

			announcementRepository.deleteAll(persistedAnnouncementEntities);
			return persistedAnnouncementEntities;
		} catch (IOException e) {
			LOGGER.error(e);
		}
		return Collections.emptyList();
	}

	private List<AnnouncementEntity> getCurrentAnnouncementsFromPage(TopicEntity topicEntity) throws IOException {
		List<AnnouncementEntity> announcementEntities = new ArrayList<>();
		final Document doc = Jsoup.connect(topicEntity.getAnnouncementLink()).get();
		final Elements links = doc.select(topicEntity.getAnnSelector());
		for (Element link : links) {
			announcementEntities.add(buildAnnouncement(topicEntity, link));
		}
		return announcementEntities;
	}

	@Async
	@SneakyThrows
	void checkAnnouncements(final TopicEntity topicEntity) {
		final Document doc = Jsoup.connect(topicEntity.getAnnouncementLink()).get();
		final Elements links = doc.select(topicEntity.getAnnSelector());
		LOGGER.debug("{} links are going to be checked with general method", links.size());
		processLinks(topicEntity, links);
	}

	private void processLinks(final TopicEntity topicEntity, final Elements links) {
		LOGGER.debug("processing topic: {}", topicEntity);
		for (final Element link : links) {
			try {
				//TODO Announcement builder from html element
				var announcement = buildAnnouncement(topicEntity, link);
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

	private AnnouncementEntity buildAnnouncement(TopicEntity topicEntity, Element htmlElement) {
		var announcement = new AnnouncementEntity();
		if (ObjectUtils.isEmpty(topicEntity.getAnnTitleSelector())) {
			announcement.setTitle(htmlElement.html());
		} else {
			announcement.setTitle(htmlElement.selectFirst(topicEntity.getAnnTitleSelector()).html());
		}
		announcement.setTopic(topicEntity);
		if (ObjectUtils.isEmpty(topicEntity.getAnnLinkSelector())) {
			announcement.setLink(htmlElement.attr("href"));
		} else {
			announcement.setLink(htmlElement.selectFirst(topicEntity.getAnnLinkSelector()).attr("href"));
		}
		return announcement;
	}

	private void notifySubscribers(final AnnouncementEntity announcementEntity) {
		for (SubscriptionEntity subscriptionEntity : subscriptionRepository.findAllByTopic(announcementEntity.getTopic())) {
			telegramService.sendMessage(subscriptionEntity.getUser(), announcementEntity.toString());
		}
	}
}
