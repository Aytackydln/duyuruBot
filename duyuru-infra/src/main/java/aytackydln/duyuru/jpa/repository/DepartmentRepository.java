package aytackydln.duyuru.jpa.repository;

import org.springframework.data.repository.CrudRepository;

import aytackydln.duyuru.jpa.entity.DepartmentEntity;

public interface DepartmentRepository extends CrudRepository<DepartmentEntity, String> {
}
