package com.enterprise.taskresource.repository;

import com.enterprise.taskresource.entity.AllocationStatus;
import com.enterprise.taskresource.entity.ResourceAllocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResourceAllocationRepository extends JpaRepository<ResourceAllocation, Long> {
    List<ResourceAllocation> findByAllocatedToId(Long userId);
    List<ResourceAllocation> findByResourceId(Long resourceId);
    List<ResourceAllocation> findByTaskId(Long taskId);
    List<ResourceAllocation> findByStatus(AllocationStatus status);
    long countByStatus(AllocationStatus status);
}
