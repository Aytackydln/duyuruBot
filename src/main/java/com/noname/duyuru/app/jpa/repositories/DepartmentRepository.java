package com.noname.duyuru.app.jpa.repositories;

import org.springframework.data.repository.CrudRepository;

import com.noname.duyuru.app.jpa.models.Department;

public interface DepartmentRepository extends CrudRepository<Department, String> {
}
