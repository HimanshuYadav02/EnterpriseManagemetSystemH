package com.enterprise.taskresource.repository;

import com.enterprise.taskresource.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
    Optional<Department> findByCode(String code);
    Boolean existsByCode(String code);
    Boolean existsByName(String name);
}
