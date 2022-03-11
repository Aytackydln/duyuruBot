package aytackydln.duyuru.topic.port;

import aytackydln.duyuru.topic.Topic;

import java.util.List;

public interface TopicPort {
    List<Topic> getAllNonDepartment();
    List<Topic> getSubscribedTopics();

    boolean existsByBoardAppend(String boardAppend);

    void update(Topic topic);

    void deleteAll(List<Topic> topics);

    List<Topic> getByDepartmentId(String id);

    List<Topic> retrieveAll();
}
