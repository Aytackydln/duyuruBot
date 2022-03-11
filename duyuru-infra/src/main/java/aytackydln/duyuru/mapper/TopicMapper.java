package aytackydln.duyuru.mapper;

import aytackydln.duyuru.jpa.entity.TopicEntity;
import aytackydln.duyuru.mapper.conf.DuyuruMapperConfig;
import aytackydln.duyuru.topic.Topic;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(
        config = DuyuruMapperConfig.class
)
public interface TopicMapper {

    Topic mapFromEntity(TopicEntity entity);
    TopicEntity map(Topic topic);

    List<Topic> mapFromEntity(List<TopicEntity> topics);
    List<TopicEntity> map(List<Topic> topics);
}
