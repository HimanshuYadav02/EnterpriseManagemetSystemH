package com.enterprise.taskresource.service;

import com.enterprise.taskresource.config.JwtUtils;
import com.enterprise.taskresource.dto.AuthRequest;
import com.enterprise.taskresource.dto.AuthResponse;
import com.enterprise.taskresource.dto.RegisterRequest;
import com.enterprise.taskresource.dto.UserResponse;
import com.enterprise.taskresource.entity.Department;
import com.enterprise.taskresource.entity.Role;
import com.enterprise.taskresource.entity.User;
import com.enterprise.taskresource.exception.BadRequestException;
import com.enterprise.taskresource.exception.ResourceNotFoundException;
import com.enterprise.taskresource.repository.DepartmentRepository;
import com.enterprise.taskresource.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    public AuthService(AuthenticationManager authenticationManager,
                       UserRepository userRepository,
                       DepartmentRepository departmentRepository,
                       PasswordEncoder passwordEncoder,
                       JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.departmentRepository = departmentRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
    }

    public AuthResponse authenticate(AuthRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + request.getUsername()));

        String token = jwtUtils.generateToken(user.getUsername(), user.getRole().name());

        return new AuthResponse(
                token,
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFullName(),
                user.getRole().name(),
                user.getDepartment() != null ? user.getDepartment().getName() : null
        );
    }

    @Transactional
    public UserResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new BadRequestException("Username is already taken: " + request.getUsername());
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email is already registered: " + request.getEmail());
        }

        Role role = Role.ROLE_EMPLOYEE;
        if (request.getRole() != null && !request.getRole().isBlank()) {
            try {
                role = Role.valueOf(request.getRole().trim());
            } catch (IllegalArgumentException e) {
                throw new BadRequestException("Invalid role specified. Allowed: ROLE_ADMIN, ROLE_MANAGER, ROLE_EMPLOYEE");
            }
        }

        Department department = null;
        if (request.getDepartmentId() != null) {
            department = departmentRepository.findById(request.getDepartmentId())
                    .orElseThrow(() -> new ResourceNotFoundException("Department not found with ID: " + request.getDepartmentId()));
        }

        User user = new User(
                request.getUsername().trim(),
                passwordEncoder.encode(request.getPassword()),
                request.getFullName().trim(),
                request.getEmail().trim().toLowerCase(),
                role,
                department
        );

        User saved = userRepository.save(user);

        return new UserResponse(
                saved.getId(),
                saved.getUsername(),
                saved.getFullName(),
                saved.getEmail(),
                saved.getRole().name(),
                saved.getDepartment() != null ? saved.getDepartment().getId() : null,
                saved.getDepartment() != null ? saved.getDepartment().getName() : null
        );
    }

    public User getCurrentAuthenticatedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
            throw new BadRequestException("User is not authenticated");
        }
        return userRepository.findByUsername(auth.getName())
                .orElseThrow(() -> new ResourceNotFoundException("Authenticated user record not found"));
    }
}
