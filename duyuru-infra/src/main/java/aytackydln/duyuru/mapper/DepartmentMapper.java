package aytackydln.duyuru.mapper;

import aytackydln.duyuru.jpa.entity.DepartmentEntity;
import aytackydln.duyuru.mapper.conf.DuyuruMapperConfig;
import aytackydln.duyuru.topic.Department;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(config = DuyuruMapperConfig.class)
public interface DepartmentMapper {

    Department mapFromEntity(DepartmentEntity departmentEntity);

    List<Department> mapFromEntity(Iterable<DepartmentEntity> all);
}
