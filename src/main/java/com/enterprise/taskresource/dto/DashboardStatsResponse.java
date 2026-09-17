package com.enterprise.taskresource.dto;

public class DashboardStatsResponse {
    private long totalTasks;
    private long completedTasks;
    private long inProgressTasks;
    private long todoTasks;
    private long inReviewTasks;
    private long overdueTasks;

    private long totalResources;
    private long availableResources;
    private long allocatedResources;
    private long maintenanceResources;
    private long activeAllocations;

    private double taskCompletionRate;
    private double resourceUtilizationRate;

    public DashboardStatsResponse() {}

    public long getTotalTasks() { return totalTasks; }
    public void setTotalTasks(long totalTasks) { this.totalTasks = totalTasks; }

    public long getCompletedTasks() { return completedTasks; }
    public void setCompletedTasks(long completedTasks) { this.completedTasks = completedTasks; }

    public long getInProgressTasks() { return inProgressTasks; }
    public void setInProgressTasks(long inProgressTasks) { this.inProgressTasks = inProgressTasks; }

    public long getTodoTasks() { return todoTasks; }
    public void setTodoTasks(long todoTasks) { this.todoTasks = todoTasks; }

    public long getInReviewTasks() { return inReviewTasks; }
    public void setInReviewTasks(long inReviewTasks) { this.inReviewTasks = inReviewTasks; }

    public long getOverdueTasks() { return overdueTasks; }
    public void setOverdueTasks(long overdueTasks) { this.overdueTasks = overdueTasks; }

    public long getTotalResources() { return totalResources; }
    public void setTotalResources(long totalResources) { this.totalResources = totalResources; }

    public long getAvailableResources() { return availableResources; }
    public void setAvailableResources(long availableResources) { this.availableResources = availableResources; }

    public long getAllocatedResources() { return allocatedResources; }
    public void setAllocatedResources(long allocatedResources) { this.allocatedResources = allocatedResources; }

    public long getMaintenanceResources() { return maintenanceResources; }
    public void setMaintenanceResources(long maintenanceResources) { this.maintenanceResources = maintenanceResources; }

    public long getActiveAllocations() { return activeAllocations; }
    public void setActiveAllocations(long activeAllocations) { this.activeAllocations = activeAllocations; }

    public double getTaskCompletionRate() { return taskCompletionRate; }
    public void setTaskCompletionRate(double taskCompletionRate) { this.taskCompletionRate = taskCompletionRate; }

    public double getResourceUtilizationRate() { return resourceUtilizationRate; }
    public void setResourceUtilizationRate(double resourceUtilizationRate) { this.resourceUtilizationRate = resourceUtilizationRate; }
}
