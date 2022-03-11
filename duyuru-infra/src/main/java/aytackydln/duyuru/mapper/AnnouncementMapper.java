package aytackydln.duyuru.mapper;

import aytackydln.duyuru.announcement.Announcement;
import aytackydln.duyuru.jpa.entity.AnnouncementEntity;
import aytackydln.duyuru.mapper.conf.DuyuruMapperConfig;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(
        config = DuyuruMapperConfig.class,
        uses = {TopicMapper.class}
)
public interface AnnouncementMapper {
    Announcement map(AnnouncementEntity entity);
    AnnouncementEntity map(Announcement announcement);
    List<Announcement> map(List<AnnouncementEntity> announcements);
}
