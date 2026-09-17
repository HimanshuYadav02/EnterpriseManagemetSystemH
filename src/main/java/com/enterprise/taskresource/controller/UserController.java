package com.enterprise.taskresource.controller;

import com.enterprise.taskresource.dto.ApiResponse;
import com.enterprise.taskresource.dto.UserResponse;
import com.enterprise.taskresource.entity.Department;
import com.enterprise.taskresource.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers() {
        List<UserResponse> users = userService.getAllUsers();
        return ResponseEntity.ok(ApiResponse.ok("Users retrieved successfully", users));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(@PathVariable Long id) {
        UserResponse user = userService.getUserById(id);
        return ResponseEntity.ok(ApiResponse.ok("User retrieved successfully", user));
    }

    @GetMapping("/departments")
    public ResponseEntity<ApiResponse<List<Department>>> getAllDepartments() {
        List<Department> departments = userService.getAllDepartments();
        return ResponseEntity.ok(ApiResponse.ok("Departments retrieved successfully", departments));
    }
}
