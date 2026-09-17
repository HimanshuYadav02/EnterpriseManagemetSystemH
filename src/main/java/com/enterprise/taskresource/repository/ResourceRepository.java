package com.enterprise.taskresource.repository;

import com.enterprise.taskresource.entity.Resource;
import com.enterprise.taskresource.entity.ResourceStatus;
import com.enterprise.taskresource.entity.ResourceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResourceRepository extends JpaRepository<Resource, Long> {
    List<Resource> findByType(ResourceType type);
    List<Resource> findByStatus(ResourceStatus status);
    List<Resource> findByDepartmentId(Long departmentId);
    long countByStatus(ResourceStatus status);
}
