package aytackydln.duyuru.service;

import aytackydln.duyuru.service.telegram.TelegramService;
import aytackydln.duyuru.jpa.models.DepartmentEntity;
import aytackydln.duyuru.jpa.models.TopicEntity;
import aytackydln.duyuru.jpa.repository.DepartmentRepository;
import aytackydln.duyuru.jpa.repository.TopicRepository;
import aytackydln.duyuru.mvc.message.SuccessMessage;
import aytackydln.duyuru.mvc.message.ViewMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class TopicUpdater {
	private final TopicRepository topicRepository;
	private final DepartmentRepository departmentRepository;
	private final TelegramService telegramService;

	public Collection<ViewMessage> updateTopics() {
		final List<ViewMessage> result = new ArrayList<>();
		final Iterable<DepartmentEntity> departments = departmentRepository.findAll();
		for (final DepartmentEntity d : departments)
			result.add(new SuccessMessage(d.getId() + ": " + checkDepartmentClasses(d)));
		return result;
	}

	private String checkDepartmentClasses(final DepartmentEntity departmentEntity) {
		try {
			final Document doc = Jsoup.connect(departmentEntity.getBaseLink() + departmentEntity.getClassesUri()).get();
			final Elements courseLinks = doc.select(departmentEntity.getClassElementSelector());

			int addedCount = 0;

			final List<TopicEntity> currentCourseTopicEntities = topicRepository.getByDepartmentId(departmentEntity.getId());
			//TODO hatada duraklama ekle

			final StringBuilder addedTopicsString = new StringBuilder();
			for (final Element link : courseLinks) {
				final String courseAppend = link.attr("href");
				final String courseId = link.html();

				final TopicEntity topicEntityFromHtml = new TopicEntity();    //TODO topic builder ile oluştur
				topicEntityFromHtml.setId(courseId);
				topicEntityFromHtml.setDepartmentId(departmentEntity.getId());

				if (!topicRepository.existsByBoardAppend(courseAppend)) { //when new topic (by link) is found
					topicEntityFromHtml.setBoardAppend(courseAppend);
					topicEntityFromHtml.setAnnSelector(departmentEntity.getClassAnnSelector());
					topicEntityFromHtml.setAnnTitleSelector(departmentEntity.getClassAnnTitleSelector());
					topicEntityFromHtml.setAnnLinkSelector(departmentEntity.getClassAnnLinkSelector());
					topicEntityFromHtml.setBaseLink(departmentEntity.getBaseLink());

					topicRepository.save(topicEntityFromHtml);
					addedTopicsString.append(topicEntityFromHtml.getId());
					addedTopicsString.append(',');
					addedCount++;

					telegramService.sendMessageToMaster("New topic\n" + topicEntityFromHtml);
				} else { //when course is already in database
					currentCourseTopicEntities.remove(topicEntityFromHtml); //remove the topic from the list, remaining ones will be deleted
				}
			}
			LOGGER.info("Added new topics: " + addedTopicsString.toString());

			//delete topics if they are not in the page
			List<String> deletedTopicNames = currentCourseTopicEntities.stream().map(TopicEntity::getId).collect(Collectors.toList());
			String deletedTopicsString = String.join(",\n", deletedTopicNames);
			LOGGER.info("Deleting " + currentCourseTopicEntities.size() + " topic(s): " + deletedTopicsString);
			topicRepository.deleteAll(currentCourseTopicEntities);
			String output = "added " + addedCount + "<br> deleted " + currentCourseTopicEntities.size() + " topics<br>";
			if (addedCount > 0) {
				output += "New: " + addedTopicsString + "<br>";
			}
			if (currentCourseTopicEntities.size() > 0) {
				output += "<br>Deleted: " + deletedTopicsString + "<br>";
				telegramService.sendMessageToMaster("Deleted topics: " + deletedTopicsString);
			}
			return output;
		} catch (IOException e) {
			LOGGER.info(departmentEntity.getId()+" classes could not be loaded ("+e.getClass().getSimpleName()+")");
			LOGGER.trace(e);
			return e.getMessage();
		}
	}
}
