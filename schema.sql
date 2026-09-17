-- =======================================================
-- Enterprise Task & Resource Management System
-- Complete MySQL Schema & Initial Data
-- =======================================================

CREATE DATABASE IF NOT EXISTS `task_resource_db`
    DEFAULT CHARACTER SET utf8mb4 
    COLLATE utf8mb4_unicode_ci;

USE `task_resource_db`;

-- 1. Departments Table
CREATE TABLE IF NOT EXISTS `departments` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(100) NOT NULL UNIQUE,
    `code` VARCHAR(20) NOT NULL UNIQUE,
    `description` VARCHAR(255),
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- 2. Users Table
CREATE TABLE IF NOT EXISTS `users` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `username` VARCHAR(50) NOT NULL UNIQUE,
    `password` VARCHAR(255) NOT NULL,
    `full_name` VARCHAR(100) NOT NULL,
    `email` VARCHAR(100) NOT NULL UNIQUE,
    `role` VARCHAR(30) NOT NULL,
    `department_id` BIGINT,
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT `fk_users_department` FOREIGN KEY (`department_id`) REFERENCES `departments` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB;

-- 3. Tasks Table
CREATE TABLE IF NOT EXISTS `tasks` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `title` VARCHAR(150) NOT NULL,
    `description` TEXT,
    `status` VARCHAR(20) NOT NULL DEFAULT 'TODO',
    `priority` VARCHAR(20) NOT NULL DEFAULT 'MEDIUM',
    `due_date` DATE,
    `estimated_hours` DOUBLE DEFAULT 0.0,
    `actual_hours` DOUBLE DEFAULT 0.0,
    `assigned_to_user_id` BIGINT,
    `created_by_user_id` BIGINT NOT NULL,
    `department_id` BIGINT,
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT `fk_tasks_assigned_user` FOREIGN KEY (`assigned_to_user_id`) REFERENCES `users` (`id`) ON DELETE SET NULL,
    CONSTRAINT `fk_tasks_creator_user` FOREIGN KEY (`created_by_user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_tasks_department` FOREIGN KEY (`department_id`) REFERENCES `departments` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB;

-- 4. Resources Table
CREATE TABLE IF NOT EXISTS `resources` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(100) NOT NULL,
    `type` VARCHAR(30) NOT NULL,
    `serial_number` VARCHAR(100),
    `status` VARCHAR(20) NOT NULL DEFAULT 'AVAILABLE',
    `total_capacity` INT NOT NULL DEFAULT 1,
    `available_capacity` INT NOT NULL DEFAULT 1,
    `location` VARCHAR(200),
    `description` TEXT,
    `department_id` BIGINT,
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT `fk_resources_department` FOREIGN KEY (`department_id`) REFERENCES `departments` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB;

-- 5. Resource Allocations Table
CREATE TABLE IF NOT EXISTS `resource_allocations` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `resource_id` BIGINT NOT NULL,
    `task_id` BIGINT,
    `user_id` BIGINT NOT NULL,
    `allocated_quantity` INT NOT NULL DEFAULT 1,
    `start_date` DATE NOT NULL,
    `end_date` DATE NOT NULL,
    `status` VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    `notes` VARCHAR(255),
    `allocated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `returned_at` DATETIME NULL,
    CONSTRAINT `fk_allocations_resource` FOREIGN KEY (`resource_id`) REFERENCES `resources` (`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_allocations_task` FOREIGN KEY (`task_id`) REFERENCES `tasks` (`id`) ON DELETE SET NULL,
    CONSTRAINT `fk_allocations_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB;
