package com.noname.duyuru.app.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import com.noname.duyuru.app.jpa.models.Department;
import com.noname.duyuru.app.jpa.models.Topic;
import com.noname.duyuru.app.jpa.models.TopicType;
import com.noname.duyuru.app.jpa.repositories.DepartmentRepository;
import com.noname.duyuru.app.jpa.repositories.TopicRepository;
import com.noname.duyuru.app.mvc.message.IViewMessage;
import com.noname.duyuru.app.mvc.message.SuccessMessage;

@Service
public class TopicUpdater {
	private static final Logger LOGGER = LogManager.getLogger(TopicUpdater.class);

	private final TopicRepository topicRepository;
	private final DepartmentRepository departmentRepository;

	public TopicUpdater(final TopicRepository topicRepository, final DepartmentRepository departmentRepository) {
		this.topicRepository = topicRepository;
		this.departmentRepository = departmentRepository;

		updateTopics();
	}

	public Collection<IViewMessage> updateTopics() {
		final List<IViewMessage> result=new ArrayList<>();
		final Iterable<Department> departments=departmentRepository.findAll();
		for (final Department d : departments)
			result.add(new SuccessMessage(d.getId()+": "+checkDepartmentClasses(d)));
		return result;
	}

	private String checkDepartmentClasses(final Department department) {
		try {
			final Document doc = Jsoup.connect(department.getBaseLink() + department.getClassesUri()).get();
			final Elements courseLinks = doc.select(department.getClassElementSelector());

			int added = 0;

			final List<Topic> currentCourseTopics = topicRepository.getByType(TopicType.CENG_CLASS);
			//TODO hatada duraklama ekle

			final StringBuilder addedTopicsString=new StringBuilder();
			for (final Element link : courseLinks) {
				final String courseAppend = link.attr("href");
				final String courseId = link.html();

				final Topic topicFromHtml = new Topic();
				topicFromHtml.setId(courseId);

				if (!topicRepository.existsById(courseId)) { //when new topic is found
					topicFromHtml.setBoardAppend(courseAppend);
					//TODO change these to department
					topicFromHtml.setBaseLink(department.getBaseLink());
					topicFromHtml.setType(TopicType.CENG_CLASS);

					topicRepository.save(topicFromHtml);
					addedTopicsString.append(topicFromHtml.getId());
					addedTopicsString.append(',');
					added++;
				} else { //when course is already in database
					currentCourseTopics.remove(topicFromHtml); //remove the topic from the list, remaining ones will be deleted
				}
			}
			LOGGER.info("Added new topics: "+addedTopicsString.toString());

			//delete topics if they are not in the page
			List<String> deletedTopicNames=currentCourseTopics.stream().map(Topic::getId).collect(Collectors.toList());
			String deletedTopicsString=String.join(",", deletedTopicNames);
			LOGGER.info("Deleting " + currentCourseTopics.size() + " topic(s): "+deletedTopicsString);
			topicRepository.deleteAll(currentCourseTopics);
			return "deleted " + currentCourseTopics.size() + "\n added " + added + " topics\n"+deletedTopicsString;
		} catch (IOException e) {
			LOGGER.info(department.getId()+" classes could not be loaded ("+e.getClass().getSimpleName()+")");
			LOGGER.trace(e);
			return e.getMessage();
		}
	}
}
