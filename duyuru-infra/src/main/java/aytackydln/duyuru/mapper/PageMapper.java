package aytackydln.duyuru.mapper;

import aytackydln.duyuru.common.PageDetails;
import aytackydln.duyuru.mapper.conf.DuyuruMapperConfig;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Mapper(config = DuyuruMapperConfig.class)
public interface PageMapper {

    default PageRequest map(PageDetails pageDetails) {
        return PageRequest.of(
                pageDetails.getPage(),
                pageDetails.getSize()
        );
    }

    @Mapping(target = "size", source = "pageable.pageSize")
    @Mapping(target = "page", source = "pageable.pageNumber")
    PageDetails map(Pageable pageable, long totalElements);
}
