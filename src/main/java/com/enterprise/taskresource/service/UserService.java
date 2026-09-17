package com.enterprise.taskresource.service;

import com.enterprise.taskresource.dto.UserResponse;
import com.enterprise.taskresource.entity.Department;
import com.enterprise.taskresource.entity.Role;
import com.enterprise.taskresource.entity.User;
import com.enterprise.taskresource.exception.ResourceNotFoundException;
import com.enterprise.taskresource.repository.DepartmentRepository;
import com.enterprise.taskresource.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;

    public UserService(UserRepository userRepository, DepartmentRepository departmentRepository) {
        this.userRepository = userRepository;
        this.departmentRepository = departmentRepository;
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::mapToUserResponse)
                .collect(Collectors.toList());
    }

    public UserResponse getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        return mapToUserResponse(user);
    }

    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    public UserResponse mapToUserResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getFullName(),
                user.getEmail(),
                user.getRole().name(),
                user.getDepartment() != null ? user.getDepartment().getId() : null,
                user.getDepartment() != null ? user.getDepartment().getName() : null
        );
    }
}
