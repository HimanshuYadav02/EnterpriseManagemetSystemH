package com.enterprise.taskresource.controller;

import com.enterprise.taskresource.dto.AllocationRequest;
import com.enterprise.taskresource.dto.AllocationResponse;
import com.enterprise.taskresource.dto.ApiResponse;
import com.enterprise.taskresource.entity.User;
import com.enterprise.taskresource.service.AllocationService;
import com.enterprise.taskresource.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/allocations")
public class AllocationController {

    private final AllocationService allocationService;
    private final AuthService authService;

    public AllocationController(AllocationService allocationService, AuthService authService) {
        this.allocationService = allocationService;
        this.authService = authService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<AllocationResponse>>> getAllAllocations() {
        User currentUser = authService.getCurrentAuthenticatedUser();
        List<AllocationResponse> allocations = allocationService.getAllAllocations(currentUser);
        return ResponseEntity.ok(ApiResponse.ok("Allocations retrieved successfully", allocations));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AllocationResponse>> getAllocationById(@PathVariable Long id) {
        AllocationResponse allocation = allocationService.getAllocationById(id);
        return ResponseEntity.ok(ApiResponse.ok("Allocation retrieved successfully", allocation));
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
    public ResponseEntity<ApiResponse<AllocationResponse>> allocateResource(@Valid @RequestBody AllocationRequest request) {
        AllocationResponse allocated = allocationService.allocateResource(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.ok("Resource allocated successfully", allocated));
    }

    @PostMapping("/{id}/return")
    public ResponseEntity<ApiResponse<AllocationResponse>> returnResource(@PathVariable Long id) {
        AllocationResponse returned = allocationService.returnResource(id);
        return ResponseEntity.ok(ApiResponse.ok("Resource returned successfully", returned));
    }

    @PostMapping("/{id}/cancel")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
    public ResponseEntity<ApiResponse<AllocationResponse>> cancelAllocation(@PathVariable Long id) {
        AllocationResponse cancelled = allocationService.cancelAllocation(id);
        return ResponseEntity.ok(ApiResponse.ok("Allocation cancelled successfully", cancelled));
    }
}
