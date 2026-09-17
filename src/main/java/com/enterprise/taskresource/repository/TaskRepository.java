package com.enterprise.taskresource.repository;

import com.enterprise.taskresource.entity.Task;
import com.enterprise.taskresource.entity.TaskPriority;
import com.enterprise.taskresource.entity.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByAssignedToId(Long userId);
    List<Task> findByCreatedById(Long userId);
    List<Task> findByStatus(TaskStatus status);
    List<Task> findByPriority(TaskPriority priority);
    List<Task> findByDepartmentId(Long departmentId);
    long countByStatus(TaskStatus status);
    long countByDueDateBeforeAndStatusNot(LocalDate date, TaskStatus status);
}
