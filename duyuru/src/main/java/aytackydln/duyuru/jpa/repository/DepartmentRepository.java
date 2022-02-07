package aytackydln.duyuru.jpa.repository;

import org.springframework.data.repository.CrudRepository;

import aytackydln.duyuru.jpa.models.DepartmentEntity;

public interface DepartmentRepository extends CrudRepository<DepartmentEntity, String> {
}
