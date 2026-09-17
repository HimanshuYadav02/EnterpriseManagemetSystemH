package com.enterprise.taskresource.config;

import com.enterprise.taskresource.entity.*;
import com.enterprise.taskresource.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final ResourceRepository resourceRepository;
    private final TaskRepository taskRepository;
    private final ResourceAllocationRepository allocationRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository,
                           DepartmentRepository departmentRepository,
                           ResourceRepository resourceRepository,
                           TaskRepository taskRepository,
                           ResourceAllocationRepository allocationRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.departmentRepository = departmentRepository;
        this.resourceRepository = resourceRepository;
        this.taskRepository = taskRepository;
        this.allocationRepository = allocationRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (departmentRepository.count() == 0) {
            logger.info("Initializing enterprise starter departments...");
            Department eng = departmentRepository.save(new Department("Engineering", "ENG", "Software Development & Architecture"));
            Department it = departmentRepository.save(new Department("IT Infrastructure", "IT", "Cloud, Hardware & Security Ops"));
            Department prod = departmentRepository.save(new Department("Product & UX", "PROD", "Product Management and Design"));
            Department ops = departmentRepository.save(new Department("Operations", "OPS", "Business and Facility Operations"));

            logger.info("Initializing enterprise default user accounts...");
            User admin = new User(
                    "admin",
                    passwordEncoder.encode("Admin@123"),
                    "Enterprise Administrator",
                    "admin@enterprise.local",
                    Role.ROLE_ADMIN,
                    it
            );
            userRepository.save(admin);

            User manager = new User(
                    "manager",
                    passwordEncoder.encode("Manager@123"),
                    "Priya Sharma",
                    "manager@enterprise.local",
                    Role.ROLE_MANAGER,
                    eng
            );
            userRepository.save(manager);

            User employee = new User(
                    "employee",
                    passwordEncoder.encode("Employee@123"),
                    "Rahul Verma",
                    "employee@enterprise.local",
                    Role.ROLE_EMPLOYEE,
                    eng
            );
            userRepository.save(employee);

            logger.info("Initializing sample resources...");
            Resource macbook = new Resource();
            macbook.setName("MacBook Pro M3 Max 64GB");
            macbook.setType(ResourceType.EQUIPMENT);
            macbook.setSerialNumber("APL-MBP-2026-001");
            macbook.setTotalCapacity(5);
            macbook.setAvailableCapacity(4);
            macbook.setStatus(ResourceStatus.AVAILABLE);
            macbook.setLocation("IT Asset Lab - Rack 4");
            macbook.setDescription("High performance development laptop for senior engineers");
            macbook.setDepartment(it);
            resourceRepository.save(macbook);

            Resource k8sCluster = new Resource();
            k8sCluster.setName("Kubernetes Staging Cluster (AWS)");
            k8sCluster.setType(ResourceType.SERVER);
            k8sCluster.setSerialNumber("SRV-K8S-STG-09");
            k8sCluster.setTotalCapacity(10);
            k8sCluster.setAvailableCapacity(8);
            k8sCluster.setStatus(ResourceStatus.AVAILABLE);
            k8sCluster.setLocation("AWS us-east-1 VPC");
            k8sCluster.setDescription("Dedicated staging environment for microservices testing");
            k8sCluster.setDepartment(eng);
            resourceRepository.save(k8sCluster);

            Resource figmaLicense = new Resource();
            figmaLicense.setName("Figma Enterprise Seat");
            figmaLicense.setType(ResourceType.SOFTWARE_LICENSE);
            figmaLicense.setSerialNumber("LIC-FIGMA-2026");
            figmaLicense.setTotalCapacity(20);
            figmaLicense.setAvailableCapacity(18);
            figmaLicense.setStatus(ResourceStatus.AVAILABLE);
            figmaLicense.setLocation("Cloud SaaS License Pool");
            figmaLicense.setDescription("Full design and prototyping workspace license");
            figmaLicense.setDepartment(prod);
            resourceRepository.save(figmaLicense);

            Resource confRoom = new Resource();
            confRoom.setName("Executive Boardroom - Horizon");
            confRoom.setType(ResourceType.MEETING_ROOM);
            confRoom.setSerialNumber("ROOM-HORIZON-FL4");
            confRoom.setTotalCapacity(1);
            confRoom.setAvailableCapacity(1);
            confRoom.setStatus(ResourceStatus.AVAILABLE);
            confRoom.setLocation("Floor 4, West Wing");
            confRoom.setDescription("20-person video-conference enabled meeting room");
            confRoom.setDepartment(ops);
            resourceRepository.save(confRoom);

            logger.info("Initializing sample tasks...");
            Task task1 = new Task();
            task1.setTitle("Implement Microservice Authentication Gateway");
            task1.setDescription("Integrate OAuth2/JWT security filters across all internal APIs with rate limiting.");
            task1.setStatus(TaskStatus.IN_PROGRESS);
            task1.setPriority(TaskPriority.HIGH);
            task1.setDueDate(LocalDate.now().plusDays(5));
            task1.setEstimatedHours(32.0);
            task1.setActualHours(12.0);
            task1.setAssignedTo(employee);
            task1.setCreatedBy(manager);
            task1.setDepartment(eng);
            taskRepository.save(task1);

            Task task2 = new Task();
            task2.setTitle("Database Migration to MySQL 8 Cluster");
            task2.setDescription("Migrate legacy schemas to optimized partitioning and read replicas.");
            task2.setStatus(TaskStatus.TODO);
            task2.setPriority(TaskPriority.URGENT);
            task2.setDueDate(LocalDate.now().plusDays(2));
            task2.setEstimatedHours(40.0);
            task2.setActualHours(0.0);
            task2.setAssignedTo(employee);
            task2.setCreatedBy(admin);
            task2.setDepartment(it);
            taskRepository.save(task2);

            Task task3 = new Task();
            task3.setTitle("UI Design System Component Library");
            task3.setDescription("Build accessible, responsive UI tokens and components for unified dashboard.");
            task3.setStatus(TaskStatus.COMPLETED);
            task3.setPriority(TaskPriority.MEDIUM);
            task3.setDueDate(LocalDate.now().minusDays(1));
            task3.setEstimatedHours(20.0);
            task3.setActualHours(18.5);
            task3.setAssignedTo(employee);
            task3.setCreatedBy(manager);
            task3.setDepartment(prod);
            taskRepository.save(task3);

            logger.info("Initializing sample resource allocations...");
            ResourceAllocation alloc1 = new ResourceAllocation();
            alloc1.setResource(macbook);
            alloc1.setTask(task1);
            alloc1.setAllocatedTo(employee);
            alloc1.setAllocatedQuantity(1);
            alloc1.setStartDate(LocalDate.now());
            alloc1.setEndDate(LocalDate.now().plusDays(30));
            alloc1.setStatus(AllocationStatus.ACTIVE);
            alloc1.setNotes("Issued for microservice architecture development");
            allocationRepository.save(alloc1);

            logger.info("Enterprise demo data initialized successfully!");
        }
    }
}
