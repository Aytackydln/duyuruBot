package aytackydln.duyuru.adapter;

import aytackydln.duyuru.jpa.entity.DepartmentEntity;
import aytackydln.duyuru.jpa.repository.DepartmentRepository;
import aytackydln.duyuru.mapper.DepartmentMapper;
import aytackydln.duyuru.topic.Department;
import aytackydln.duyuru.topic.port.DepartmentPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DepartmentsAdapter implements DepartmentPort {
    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;

    @Override
    public List<Department> findAll() {
        Iterable<DepartmentEntity> all = departmentRepository.findAll();
        return departmentMapper.mapFromEntity(all);
    }
}
