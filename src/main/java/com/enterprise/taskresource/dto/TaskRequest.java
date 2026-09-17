package com.enterprise.taskresource.dto;

import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;

public class TaskRequest {
    @NotBlank(message = "Task title is required")
    private String title;

    private String description;
    private String priority; // LOW, MEDIUM, HIGH, URGENT
    private LocalDate dueDate;
    private Double estimatedHours;
    private Long assignedToUserId;
    private Long departmentId;

    public TaskRequest() {}

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }

    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

    public Double getEstimatedHours() { return estimatedHours; }
    public void setEstimatedHours(Double estimatedHours) { this.estimatedHours = estimatedHours; }

    public Long getAssignedToUserId() { return assignedToUserId; }
    public void setAssignedToUserId(Long assignedToUserId) { this.assignedToUserId = assignedToUserId; }

    public Long getDepartmentId() { return departmentId; }
    public void setDepartmentId(Long departmentId) { this.departmentId = departmentId; }
}
