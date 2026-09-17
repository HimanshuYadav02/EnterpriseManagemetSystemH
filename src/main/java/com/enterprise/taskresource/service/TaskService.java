package com.enterprise.taskresource.service;

import com.enterprise.taskresource.dto.TaskRequest;
import com.enterprise.taskresource.dto.TaskResponse;
import com.enterprise.taskresource.dto.TaskStatusUpdateRequest;
import com.enterprise.taskresource.entity.*;
import com.enterprise.taskresource.exception.BadRequestException;
import com.enterprise.taskresource.exception.ResourceNotFoundException;
import com.enterprise.taskresource.repository.DepartmentRepository;
import com.enterprise.taskresource.repository.TaskRepository;
import com.enterprise.taskresource.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;

    public TaskService(TaskRepository taskRepository,
                       UserRepository userRepository,
                       DepartmentRepository departmentRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.departmentRepository = departmentRepository;
    }

    public List<TaskResponse> getAllTasks(User currentUser) {
        List<Task> tasks;
        if (currentUser.getRole() == Role.ROLE_EMPLOYEE) {
            tasks = taskRepository.findByAssignedToId(currentUser.getId());
        } else {
            tasks = taskRepository.findAll();
        }
        return tasks.stream().map(this::mapToTaskResponse).collect(Collectors.toList());
    }

    public TaskResponse getTaskById(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with ID: " + id));
        return mapToTaskResponse(task);
    }

    @Transactional
    public TaskResponse createTask(TaskRequest request, User currentUser) {
        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStatus(TaskStatus.TODO);

        if (request.getPriority() != null) {
            try {
                task.setPriority(TaskPriority.valueOf(request.getPriority().toUpperCase()));
            } catch (IllegalArgumentException e) {
                task.setPriority(TaskPriority.MEDIUM);
            }
        }

        task.setDueDate(request.getDueDate());
        task.setEstimatedHours(request.getEstimatedHours() != null ? request.getEstimatedHours() : 0.0);
        task.setActualHours(0.0);
        task.setCreatedBy(currentUser);

        if (request.getAssignedToUserId() != null) {
            User assignedUser = userRepository.findById(request.getAssignedToUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("Assigned user not found with ID: " + request.getAssignedToUserId()));
            task.setAssignedTo(assignedUser);
        }

        if (request.getDepartmentId() != null) {
            Department department = departmentRepository.findById(request.getDepartmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Department not found with ID: " + request.getDepartmentId()));
            task.setDepartment(department);
        } else if (currentUser.getDepartment() != null) {
            task.setDepartment(currentUser.getDepartment());
        }

        Task saved = taskRepository.save(task);
        return mapToTaskResponse(saved);
    }

    @Transactional
    public TaskResponse updateTask(Long id, TaskRequest request, User currentUser) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with ID: " + id));

        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());

        if (request.getPriority() != null) {
            try {
                task.setPriority(TaskPriority.valueOf(request.getPriority().toUpperCase()));
            } catch (IllegalArgumentException e) {
                // keep current priority
            }
        }

        task.setDueDate(request.getDueDate());
        if (request.getEstimatedHours() != null) {
            task.setEstimatedHours(request.getEstimatedHours());
        }

        if (request.getAssignedToUserId() != null) {
            User assignedUser = userRepository.findById(request.getAssignedToUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("Assigned user not found"));
            task.setAssignedTo(assignedUser);
        } else {
            task.setAssignedTo(null);
        }

        if (request.getDepartmentId() != null) {
            Department dept = departmentRepository.findById(request.getDepartmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Department not found"));
            task.setDepartment(dept);
        }

        Task updated = taskRepository.save(task);
        return mapToTaskResponse(updated);
    }

    @Transactional
    public TaskResponse updateTaskStatus(Long id, TaskStatusUpdateRequest request, User currentUser) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with ID: " + id));

        try {
            TaskStatus status = TaskStatus.valueOf(request.getStatus().toUpperCase());
            task.setStatus(status);
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid status: " + request.getStatus() + ". Allowed: TODO, IN_PROGRESS, IN_REVIEW, COMPLETED");
        }

        if (request.getActualHours() != null) {
            task.setActualHours(request.getActualHours());
        }

        Task updated = taskRepository.save(task);
        return mapToTaskResponse(updated);
    }

    @Transactional
    public void deleteTask(Long id, User currentUser) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with ID: " + id));
        taskRepository.delete(task);
    }

    public TaskResponse mapToTaskResponse(Task task) {
        TaskResponse res = new TaskResponse();
        res.setId(task.getId());
        res.setTitle(task.getTitle());
        res.setDescription(task.getDescription());
        res.setStatus(task.getStatus().name());
        res.setPriority(task.getPriority().name());
        res.setDueDate(task.getDueDate());
        res.setEstimatedHours(task.getEstimatedHours());
        res.setActualHours(task.getActualHours());
        res.setCreatedAt(task.getCreatedAt());
        res.setUpdatedAt(task.getUpdatedAt());

        if (task.getAssignedTo() != null) {
            res.setAssignedToId(task.getAssignedTo().getId());
            res.setAssignedToName(task.getAssignedTo().getFullName());
        }
        if (task.getCreatedBy() != null) {
            res.setCreatedById(task.getCreatedBy().getId());
            res.setCreatedByName(task.getCreatedBy().getFullName());
        }
        if (task.getDepartment() != null) {
            res.setDepartmentId(task.getDepartment().getId());
            res.setDepartmentName(task.getDepartment().getName());
        }
        return res;
    }
}
