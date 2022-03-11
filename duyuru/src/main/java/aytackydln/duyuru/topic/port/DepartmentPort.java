package aytackydln.duyuru.topic.port;

import aytackydln.duyuru.topic.Department;

import java.util.List;

public interface DepartmentPort {
    List<Department> findAll();
}
