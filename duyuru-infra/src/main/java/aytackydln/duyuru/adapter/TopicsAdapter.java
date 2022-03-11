package aytackydln.duyuru.adapter;

import aytackydln.duyuru.jpa.repository.SubscriptionRepository;
import aytackydln.duyuru.jpa.repository.TopicRepository;
import aytackydln.duyuru.topic.Topic;
import aytackydln.duyuru.topic.port.TopicPort;
import aytackydln.duyuru.jpa.entity.TopicEntity;
import aytackydln.duyuru.mapper.TopicMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class TopicsAdapter implements TopicPort {
    private final TopicRepository topicRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final TopicMapper topicMapper;

    @Override
    public List<Topic> getAllNonDepartment() {
        List<TopicEntity> topics = topicRepository.getByDepartmentIdNotNull();
        return topicMapper.mapFromEntity(topics);
    }

    @Override
    public List<Topic> getSubscribedTopics() {
        List<TopicEntity> distinctTopics = subscriptionRepository.findDistinctTopics();
        return topicMapper.mapFromEntity(distinctTopics);
    }

    @Override
    public boolean existsByBoardAppend(String boardAppend) {
        return topicRepository.existsByBoardAppend(boardAppend);
    }

    @Override
    public void update(Topic topic) {
        TopicEntity topicEntity = topicMapper.map(topic);
        topicRepository.save(topicEntity);
    }

    @Override
    public void deleteAll(List<Topic> topics) {
        List<TopicEntity> entityList = topicMapper.map(topics);
        topicRepository.deleteAll(entityList);
    }

    @Override
    public List<Topic> getByDepartmentId(String id) {
        List<TopicEntity> topic = topicRepository.getByDepartmentId(id);
        return topicMapper.mapFromEntity(topic);
    }

    @Override
    public List<Topic> retrieveAll() {
        List<TopicEntity> topicEntityList = topicRepository.findAll();
        return topicMapper.mapFromEntity(topicEntityList);
    }
}
