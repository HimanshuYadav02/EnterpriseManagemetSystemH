package com.enterprise.taskresource.repository;

import com.enterprise.taskresource.entity.Role;
import com.enterprise.taskresource.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
    List<User> findByDepartmentId(Long departmentId);
    List<User> findByRole(Role role);
}
