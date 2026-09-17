package com.enterprise.taskresource.service;

import com.enterprise.taskresource.dto.ResourceRequest;
import com.enterprise.taskresource.dto.ResourceResponse;
import com.enterprise.taskresource.entity.Department;
import com.enterprise.taskresource.entity.Resource;
import com.enterprise.taskresource.entity.ResourceStatus;
import com.enterprise.taskresource.entity.ResourceType;
import com.enterprise.taskresource.exception.BadRequestException;
import com.enterprise.taskresource.exception.ResourceNotFoundException;
import com.enterprise.taskresource.repository.DepartmentRepository;
import com.enterprise.taskresource.repository.ResourceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ResourceService {

    private final ResourceRepository resourceRepository;
    private final DepartmentRepository departmentRepository;

    public ResourceService(ResourceRepository resourceRepository, DepartmentRepository departmentRepository) {
        this.resourceRepository = resourceRepository;
        this.departmentRepository = departmentRepository;
    }

    public List<ResourceResponse> getAllResources() {
        return resourceRepository.findAll().stream()
                .map(this::mapToResourceResponse)
                .collect(Collectors.toList());
    }

    public ResourceResponse getResourceById(Long id) {
        Resource resource = resourceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found with ID: " + id));
        return mapToResourceResponse(resource);
    }

    @Transactional
    public ResourceResponse createResource(ResourceRequest request) {
        Resource resource = new Resource();
        resource.setName(request.getName());

        try {
            resource.setType(ResourceType.valueOf(request.getType().toUpperCase()));
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid resource type: " + request.getType() + ". Allowed: EQUIPMENT, SOFTWARE_LICENSE, MEETING_ROOM, SERVER, VEHICLE");
        }

        resource.setSerialNumber(request.getSerialNumber());
        resource.setTotalCapacity(request.getTotalCapacity());
        resource.setAvailableCapacity(request.getTotalCapacity());
        resource.setStatus(ResourceStatus.AVAILABLE);
        resource.setLocation(request.getLocation());
        resource.setDescription(request.getDescription());

        if (request.getDepartmentId() != null) {
            Department dept = departmentRepository.findById(request.getDepartmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Department not found with ID: " + request.getDepartmentId()));
            resource.setDepartment(dept);
        }

        Resource saved = resourceRepository.save(resource);
        return mapToResourceResponse(saved);
    }

    @Transactional
    public ResourceResponse updateResource(Long id, ResourceRequest request) {
        Resource resource = resourceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found with ID: " + id));

        resource.setName(request.getName());
        try {
            resource.setType(ResourceType.valueOf(request.getType().toUpperCase()));
        } catch (IllegalArgumentException ignored) {}

        resource.setSerialNumber(request.getSerialNumber());
        resource.setLocation(request.getLocation());
        resource.setDescription(request.getDescription());

        int capacityDiff = request.getTotalCapacity() - resource.getTotalCapacity();
        resource.setTotalCapacity(request.getTotalCapacity());
        resource.setAvailableCapacity(Math.max(0, resource.getAvailableCapacity() + capacityDiff));

        if (resource.getAvailableCapacity() == 0) {
            resource.setStatus(ResourceStatus.ALLOCATED);
        } else if (resource.getStatus() == ResourceStatus.ALLOCATED) {
            resource.setStatus(ResourceStatus.AVAILABLE);
        }

        if (request.getDepartmentId() != null) {
            Department dept = departmentRepository.findById(request.getDepartmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Department not found"));
            resource.setDepartment(dept);
        }

        Resource updated = resourceRepository.save(resource);
        return mapToResourceResponse(updated);
    }

    @Transactional
    public void deleteResource(Long id) {
        Resource resource = resourceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Resource not found with ID: " + id));
        resourceRepository.delete(resource);
    }

    public ResourceResponse mapToResourceResponse(Resource resource) {
        ResourceResponse res = new ResourceResponse();
        res.setId(resource.getId());
        res.setName(resource.getName());
        res.setType(resource.getType().name());
        res.setSerialNumber(resource.getSerialNumber());
        res.setStatus(resource.getStatus().name());
        res.setTotalCapacity(resource.getTotalCapacity());
        res.setAvailableCapacity(resource.getAvailableCapacity());
        res.setLocation(resource.getLocation());
        res.setDescription(resource.getDescription());
        res.setCreatedAt(resource.getCreatedAt());

        if (resource.getDepartment() != null) {
            res.setDepartmentId(resource.getDepartment().getId());
            res.setDepartmentName(resource.getDepartment().getName());
        }
        return res;
    }
}
