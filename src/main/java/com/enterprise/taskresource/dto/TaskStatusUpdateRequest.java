package com.enterprise.taskresource.dto;

import jakarta.validation.constraints.NotBlank;

public class TaskStatusUpdateRequest {
    @NotBlank(message = "Status is required")
    private String status;

    private Double actualHours;

    public TaskStatusUpdateRequest() {}

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Double getActualHours() { return actualHours; }
    public void setActualHours(Double actualHours) { this.actualHours = actualHours; }
}
