package com.enterprise.taskresource.service;

import com.enterprise.taskresource.dto.DashboardStatsResponse;
import com.enterprise.taskresource.entity.AllocationStatus;
import com.enterprise.taskresource.entity.ResourceStatus;
import com.enterprise.taskresource.entity.TaskStatus;
import com.enterprise.taskresource.repository.ResourceAllocationRepository;
import com.enterprise.taskresource.repository.ResourceRepository;
import com.enterprise.taskresource.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class DashboardService {

    private final TaskRepository taskRepository;
    private final ResourceRepository resourceRepository;
    private final ResourceAllocationRepository allocationRepository;

    public DashboardService(TaskRepository taskRepository,
                            ResourceRepository resourceRepository,
                            ResourceAllocationRepository allocationRepository) {
        this.taskRepository = taskRepository;
        this.resourceRepository = resourceRepository;
        this.allocationRepository = allocationRepository;
    }

    public DashboardStatsResponse getDashboardStats() {
        DashboardStatsResponse stats = new DashboardStatsResponse();

        long totalTasks = taskRepository.count();
        long completedTasks = taskRepository.countByStatus(TaskStatus.COMPLETED);
        long inProgressTasks = taskRepository.countByStatus(TaskStatus.IN_PROGRESS);
        long todoTasks = taskRepository.countByStatus(TaskStatus.TODO);
        long inReviewTasks = taskRepository.countByStatus(TaskStatus.IN_REVIEW);
        long overdueTasks = taskRepository.countByDueDateBeforeAndStatusNot(LocalDate.now(), TaskStatus.COMPLETED);

        long totalResources = resourceRepository.count();
        long availableResources = resourceRepository.countByStatus(ResourceStatus.AVAILABLE);
        long allocatedResources = resourceRepository.countByStatus(ResourceStatus.ALLOCATED);
        long maintenanceResources = resourceRepository.countByStatus(ResourceStatus.MAINTENANCE);
        long activeAllocations = allocationRepository.countByStatus(AllocationStatus.ACTIVE);

        stats.setTotalTasks(totalTasks);
        stats.setCompletedTasks(completedTasks);
        stats.setInProgressTasks(inProgressTasks);
        stats.setTodoTasks(todoTasks);
        stats.setInReviewTasks(inReviewTasks);
        stats.setOverdueTasks(overdueTasks);

        stats.setTotalResources(totalResources);
        stats.setAvailableResources(availableResources);
        stats.setAllocatedResources(allocatedResources);
        stats.setMaintenanceResources(maintenanceResources);
        stats.setActiveAllocations(activeAllocations);

        double taskCompletionRate = totalTasks > 0 ? ((double) completedTasks / totalTasks) * 100.0 : 0.0;
        stats.setTaskCompletionRate(Math.round(taskCompletionRate * 10.0) / 10.0);

        double resourceUtilizationRate = totalResources > 0 ? (((double) (allocatedResources + activeAllocations)) / (totalResources + activeAllocations)) * 100.0 : 0.0;
        stats.setResourceUtilizationRate(Math.round(resourceUtilizationRate * 10.0) / 10.0);

        return stats;
    }
}
