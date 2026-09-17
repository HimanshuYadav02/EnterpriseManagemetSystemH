package com.enterprise.taskresource.controller;

import com.enterprise.taskresource.dto.ApiResponse;
import com.enterprise.taskresource.dto.TaskRequest;
import com.enterprise.taskresource.dto.TaskResponse;
import com.enterprise.taskresource.dto.TaskStatusUpdateRequest;
import com.enterprise.taskresource.entity.User;
import com.enterprise.taskresource.service.AuthService;
import com.enterprise.taskresource.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;
    private final AuthService authService;

    public TaskController(TaskService taskService, AuthService authService) {
        this.taskService = taskService;
        this.authService = authService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<TaskResponse>>> getAllTasks() {
        User currentUser = authService.getCurrentAuthenticatedUser();
        List<TaskResponse> tasks = taskService.getAllTasks(currentUser);
        return ResponseEntity.ok(ApiResponse.ok("Tasks retrieved successfully", tasks));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TaskResponse>> getTaskById(@PathVariable Long id) {
        TaskResponse task = taskService.getTaskById(id);
        return ResponseEntity.ok(ApiResponse.ok("Task retrieved successfully", task));
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
    public ResponseEntity<ApiResponse<TaskResponse>> createTask(@Valid @RequestBody TaskRequest request) {
        User currentUser = authService.getCurrentAuthenticatedUser();
        TaskResponse created = taskService.createTask(request, currentUser);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Task created successfully", created));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
    public ResponseEntity<ApiResponse<TaskResponse>> updateTask(@PathVariable Long id, @Valid @RequestBody TaskRequest request) {
        User currentUser = authService.getCurrentAuthenticatedUser();
        TaskResponse updated = taskService.updateTask(id, request, currentUser);
        return ResponseEntity.ok(ApiResponse.ok("Task updated successfully", updated));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<TaskResponse>> updateTaskStatus(@PathVariable Long id, @Valid @RequestBody TaskStatusUpdateRequest request) {
        User currentUser = authService.getCurrentAuthenticatedUser();
        TaskResponse updated = taskService.updateTaskStatus(id, request, currentUser);
        return ResponseEntity.ok(ApiResponse.ok("Task status updated successfully", updated));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
    public ResponseEntity<ApiResponse<Void>> deleteTask(@PathVariable Long id) {
        User currentUser = authService.getCurrentAuthenticatedUser();
        taskService.deleteTask(id, currentUser);
        return ResponseEntity.ok(ApiResponse.ok("Task deleted successfully"));
    }
}
