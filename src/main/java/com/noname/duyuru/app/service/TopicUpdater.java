package com.noname.duyuru.app.service;

import com.noname.duyuru.app.jpa.models.Department;
import com.noname.duyuru.app.jpa.models.Topic;
import com.noname.duyuru.app.jpa.repositories.DepartmentRepository;
import com.noname.duyuru.app.jpa.repositories.TopicRepository;
import com.noname.duyuru.app.mvc.message.SuccessMessage;
import com.noname.duyuru.app.mvc.message.ViewMessage;
import com.noname.duyuru.app.service.telegram.TelegramService;
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
		final Iterable<Department> departments = departmentRepository.findAll();
		for (final Department d : departments)
			result.add(new SuccessMessage(d.getId() + ": " + checkDepartmentClasses(d)));
		return result;
	}

	private String checkDepartmentClasses(final Department department) {
		try {
			final Document doc = Jsoup.connect(department.getBaseLink() + department.getClassesUri()).get();
			final Elements courseLinks = doc.select(department.getClassElementSelector());

			int addedCount = 0;

			final List<Topic> currentCourseTopics = topicRepository.getByDepartmentId(department.getId());
			//TODO hatada duraklama ekle

			final StringBuilder addedTopicsString = new StringBuilder();
			for (final Element link : courseLinks) {
				final String courseAppend = link.attr("href");
				final String courseId = link.html();

				final Topic topicFromHtml = new Topic();    //TODO topic builder ile oluştur
				topicFromHtml.setId(courseId);
				topicFromHtml.setDepartmentId(department.getId());

				if (!topicRepository.existsByBoardAppend(courseAppend)) { //when new topic (by link) is found
					topicFromHtml.setBoardAppend(courseAppend);
					topicFromHtml.setAnnSelector(department.getClassAnnSelector());
					topicFromHtml.setAnnTitleSelector(department.getClassAnnTitleSelector());
					topicFromHtml.setAnnLinkSelector(department.getClassAnnLinkSelector());
					topicFromHtml.setBaseLink(department.getBaseLink());

					topicRepository.save(topicFromHtml);
					addedTopicsString.append(topicFromHtml.getId());
					addedTopicsString.append(',');
					addedCount++;

					telegramService.sendMessageToMaster("New topic\n" + topicFromHtml);
				} else { //when course is already in database
					currentCourseTopics.remove(topicFromHtml); //remove the topic from the list, remaining ones will be deleted
				}
			}
			LOGGER.info("Added new topics: " + addedTopicsString.toString());

			//delete topics if they are not in the page
			List<String> deletedTopicNames = currentCourseTopics.stream().map(Topic::getId).collect(Collectors.toList());
			String deletedTopicsString = String.join(",\n", deletedTopicNames);
			LOGGER.info("Deleting " + currentCourseTopics.size() + " topic(s): " + deletedTopicsString);
			topicRepository.deleteAll(currentCourseTopics);
			String output = "added " + addedCount + "<br> deleted " + currentCourseTopics.size() + " topics<br>";
			if (addedCount > 0) {
				output += "New: " + addedTopicsString + "<br>";
			}
			if (currentCourseTopics.size() > 0) {
				output += "<br>Deleted: " + deletedTopicsString + "<br>";
				telegramService.sendMessageToMaster("Deleted topics: " + deletedTopicsString);
			}
			return output;
		} catch (IOException e) {
			LOGGER.info(department.getId()+" classes could not be loaded ("+e.getClass().getSimpleName()+")");
			LOGGER.trace(e);
			return e.getMessage();
		}
	}
}
