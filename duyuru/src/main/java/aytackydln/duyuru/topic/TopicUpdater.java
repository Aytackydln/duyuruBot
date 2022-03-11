package aytackydln.duyuru.topic;

import aytackydln.duyuru.chatplatform.TelegramFacade;
import aytackydln.duyuru.common.semantic.Action;
import aytackydln.duyuru.common.semantic.DomainComponent;
import aytackydln.duyuru.topic.port.DepartmentPort;
import aytackydln.duyuru.topic.port.TopicPort;
import aytackydln.duyuru.topic.port.TopicReaderPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@DomainComponent
public class TopicUpdater {
	private final TopicPort topicPort;
	private final DepartmentPort departmentRepository;
	private final TelegramFacade telegramFacade;
	private final TopicReaderPort topicReaderPort;

	@Action
	public List<String> updateTopics() {
		final List<String> result = new ArrayList<>();
		final Iterable<Department> departments = departmentRepository.findAll();
		for (final Department d : departments)
			result.add(d.getId() + ": " + checkDepartmentClasses(d));
		return result;
	}

	private String checkDepartmentClasses(Department department) {
		try {
			return tryCheckTopics(department);
		}catch (Exception e){
			return e.getMessage();
		}
	}

	private String tryCheckTopics(Department department) {
		List<Topic> topics = topicReaderPort.readTopics(department);

		final List<Topic> currentTopics = topicPort.getByDepartmentId(department.getId());
		//TODO hatada duraklama ekle

		List<Topic> addedTopics = new ArrayList<>();
		for (var topic :topics) {
			if (!topicPort.existsByBoardAppend(topic.getBoardAppend())) { //when new topic (by link) is found
				topicPort.update(topic);
				addedTopics.add(topic);

				telegramFacade.sendMessageToMaster("New topic\n" + topic);
			} else { //when course is already in database
				currentTopics.remove(topic); //remove the topic from the list, remaining ones will be deleted
			}
		}
		LOGGER.info("Added new topics: " + addedTopics);

		//delete topics if they are not in the page
		String deletedTopicsString = deleteTopics(currentTopics);

		return getString(currentTopics, addedTopics, deletedTopicsString);
	}

	private String getString(List<Topic> currentTopics, List<Topic> addedTopics, String deletedTopicsString) {
		String output = "added " + addedTopics.size() + "<br>deleted " + currentTopics.size() + " topics<br>";
		if (addedTopics.size() > 0) {
			output += "New: " + addedTopics + "<br>";
		}
		if (currentTopics.size() > 0) {
			output += "<br>Deleted: " + deletedTopicsString + "<br>";
			telegramFacade.sendMessageToMaster("Deleted topics: " + deletedTopicsString);
		}
		return output;
	}

	private String deleteTopics(List<Topic> topics) {
		List<String> deletedTopicNames = topics.stream().map(Topic::getId).collect(Collectors.toList());
		String deletedTopicsString = String.join(",\n", deletedTopicNames);
		LOGGER.info("Deleting " + topics.size() + " topic(s): " + deletedTopicsString);
		topicPort.deleteAll(topics);
		return deletedTopicsString;
	}
}
