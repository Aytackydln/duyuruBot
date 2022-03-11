package aytackydln.duyuru.topic.port;

import aytackydln.duyuru.topic.Department;
import aytackydln.duyuru.topic.Topic;

import java.util.List;

public interface TopicReaderPort {
    List<Topic> readTopics(Department department);
}
