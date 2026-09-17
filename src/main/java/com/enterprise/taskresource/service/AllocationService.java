package com.enterprise.taskresource.service;

import com.enterprise.taskresource.dto.AllocationRequest;
import com.enterprise.taskresource.dto.AllocationResponse;
import com.enterprise.taskresource.entity.*;
import com.enterprise.taskresource.exception.BadRequestException;
import com.enterprise.taskresource.exception.ResourceNotFoundException;
import com.enterprise.taskresource.repository.ResourceAllocationRepository;
import com.enterprise.taskresource.repository.ResourceRepository;
import com.enterprise.taskresource.repository.TaskRepository;
import com.enterprise.taskresource.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AllocationService {

    private final ResourceAllocationRepository allocationRepository;
    private final ResourceRepository resourceRepository;
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;

    public AllocationService(ResourceAllocationRepository allocationRepository,
                             ResourceRepository resourceRepository,
                             UserRepository userRepository,
                             TaskRepository taskRepository) {
        this.allocationRepository = allocationRepository;
        this.resourceRepository = resourceRepository;
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
    }

    public List<AllocationResponse> getAllAllocations(User currentUser) {
        List<ResourceAllocation> list;
        if (currentUser.getRole() == Role.ROLE_EMPLOYEE) {
            list = allocationRepository.findByAllocatedToId(currentUser.getId());
        } else {
            list = allocationRepository.findAll();
        }
        return list.stream().map(this::mapToAllocationResponse).collect(Collectors.toList());
    }

    public AllocationResponse getAllocationById(Long id) {
        ResourceAllocation allocation = allocationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Allocation not found with ID: " + id));
        return mapToAllocationResponse(allocation);
    }

    @Transactional
    public AllocationResponse allocateResource(AllocationRequest request) {
        Resource resource = resourceRepository.findById(request.getResourceId())
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found with ID: " + request.getResourceId()));

        if (resource.getStatus() == ResourceStatus.MAINTENANCE || resource.getStatus() == ResourceStatus.RETIRED) {
            throw new BadRequestException("Resource is currently in " + resource.getStatus() + " status and cannot be allocated.");
        }

        if (resource.getAvailableCapacity() < request.getQuantity()) {
            throw new BadRequestException("Insufficient capacity. Available: " + resource.getAvailableCapacity() + ", Requested: " + request.getQuantity());
        }

        if (request.getStartDate().isAfter(request.getEndDate())) {
            throw new BadRequestException("Start date cannot be after end date");
        }

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + request.getUserId()));

        Task task = null;
        if (request.getTaskId() != null) {
            task = taskRepository.findById(request.getTaskId())
                    .orElseThrow(() -> new ResourceNotFoundException("Task not found with ID: " + request.getTaskId()));
        }

        // Deduct available capacity
        resource.setAvailableCapacity(resource.getAvailableCapacity() - request.getQuantity());
        if (resource.getAvailableCapacity() == 0) {
            resource.setStatus(ResourceStatus.ALLOCATED);
        }
        resourceRepository.save(resource);

        ResourceAllocation allocation = new ResourceAllocation();
        allocation.setResource(resource);
        allocation.setTask(task);
        allocation.setAllocatedTo(user);
        allocation.setAllocatedQuantity(request.getQuantity());
        allocation.setStartDate(request.getStartDate());
        allocation.setEndDate(request.getEndDate());
        allocation.setStatus(AllocationStatus.ACTIVE);
        allocation.setNotes(request.getNotes());

        ResourceAllocation saved = allocationRepository.save(allocation);
        return mapToAllocationResponse(saved);
    }

    @Transactional
    public AllocationResponse returnResource(Long id) {
        ResourceAllocation allocation = allocationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Allocation not found with ID: " + id));

        if (allocation.getStatus() != AllocationStatus.ACTIVE) {
            throw new BadRequestException("Allocation is already " + allocation.getStatus());
        }

        Resource resource = allocation.getResource();
        resource.setAvailableCapacity(Math.min(resource.getTotalCapacity(), resource.getAvailableCapacity() + allocation.getAllocatedQuantity()));
        if (resource.getAvailableCapacity() > 0 && resource.getStatus() == ResourceStatus.ALLOCATED) {
            resource.setStatus(ResourceStatus.AVAILABLE);
        }
        resourceRepository.save(resource);

        allocation.setStatus(AllocationStatus.RETURNED);
        allocation.setReturnedAt(LocalDateTime.now());
        ResourceAllocation updated = allocationRepository.save(allocation);

        return mapToAllocationResponse(updated);
    }

    @Transactional
    public AllocationResponse cancelAllocation(Long id) {
        ResourceAllocation allocation = allocationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Allocation not found with ID: " + id));

        if (allocation.getStatus() == AllocationStatus.ACTIVE) {
            Resource resource = allocation.getResource();
            resource.setAvailableCapacity(Math.min(resource.getTotalCapacity(), resource.getAvailableCapacity() + allocation.getAllocatedQuantity()));
            if (resource.getAvailableCapacity() > 0 && resource.getStatus() == ResourceStatus.ALLOCATED) {
                resource.setStatus(ResourceStatus.AVAILABLE);
            }
            resourceRepository.save(resource);
        }

        allocation.setStatus(AllocationStatus.CANCELLED);
        allocation.setReturnedAt(LocalDateTime.now());
        ResourceAllocation updated = allocationRepository.save(allocation);

        return mapToAllocationResponse(updated);
    }

    public AllocationResponse mapToAllocationResponse(ResourceAllocation allocation) {
        AllocationResponse res = new AllocationResponse();
        res.setId(allocation.getId());
        res.setResourceId(allocation.getResource().getId());
        res.setResourceName(allocation.getResource().getName());
        res.setResourceType(allocation.getResource().getType().name());

        if (allocation.getTask() != null) {
            res.setTaskId(allocation.getTask().getId());
            res.setTaskTitle(allocation.getTask().getTitle());
        }

        res.setUserId(allocation.getAllocatedTo().getId());
        res.setUserName(allocation.getAllocatedTo().getFullName());
        res.setAllocatedQuantity(allocation.getAllocatedQuantity());
        res.setStartDate(allocation.getStartDate());
        res.setEndDate(allocation.getEndDate());
        res.setStatus(allocation.getStatus().name());
        res.setNotes(allocation.getNotes());
        res.setAllocatedAt(allocation.getAllocatedAt());
        res.setReturnedAt(allocation.getReturnedAt());

        return res;
    }
}
