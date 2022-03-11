package aytackydln.duyuru.mapper;

import aytackydln.duyuru.jpa.entity.UserEntity;
import aytackydln.duyuru.mapper.conf.DuyuruMapperConfig;
import aytackydln.duyuru.subscriber.Subscriber;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(
        config = DuyuruMapperConfig.class
)
public interface UserMapper {
    @Mapping(target = "name", ignore = true)
    UserEntity map(Subscriber subscriber);

    Subscriber mapFromEntity(UserEntity entity);
    List<Subscriber> mapFromEntity(List<UserEntity> entities);
}
