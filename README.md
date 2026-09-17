# Enterprise Task & Resource Management System

A robust, enterprise-grade Task & Resource Allocation system built with **Java 21/25**, **Spring Boot 3.4**, **Spring Security 6**, **JJWT (0.12.6)**, **MySQL**, and a modern **ES6+ Vanilla SPA Frontend**.

---

## 📁 Complete Folder Structure

```
enterprise-task-resource-mgmt/
├── pom.xml                                   # Maven dependencies & build configuration
├── schema.sql                                # Standalone MySQL DDL & DML database scripts
├── README.md                                 # Full documentation & setup guide
└── src/
    ├── main/
    │   ├── java/com/enterprise/taskresource/
    │   │   ├── TaskResourceManagementApplication.java  # Spring Boot Main Entrypoint
    │   │   ├── config/
    │   │   │   ├── SecurityConfig.java       # Spring Security 6 FilterChain & CORS
    │   │   │   ├── JwtAuthFilter.java        # OncePerRequestFilter for Bearer token
    │   │   │   ├── JwtUtils.java             # JJWT 0.12 token generation & verification
    │   │   │   ├── UserDetailsServiceImpl.java # Custom UserDetailsService
    │   │   │   └── DataInitializer.java      # Pre-seeds Demo accounts, assets & tasks
    │   │   ├── controller/
    │   │   │   ├── AuthController.java       # /api/auth/login, /api/auth/register, /api/auth/me
    │   │   │   ├── UserController.java       # /api/users, /api/users/departments
    │   │   │   ├── TaskController.java       # /api/tasks CRUD & status updates
    │   │   │   ├── ResourceController.java   # /api/resources inventory & management
    │   │   │   ├── AllocationController.java # /api/allocations booking & check-in/out
    │   │   │   └── DashboardController.java  # /api/dashboard/stats live KPI aggregations
    │   │   ├── dto/
    │   │   │   ├── ApiResponse.java          # Standard unified REST envelope
    │   │   │   ├── AuthRequest.java          # Login payload
    │   │   │   ├── AuthResponse.java         # Token & authenticated user profile
    │   │   │   ├── RegisterRequest.java      # User registration payload
    │   │   │   ├── UserResponse.java         # User response DTO
    │   │   │   ├── TaskRequest.java          # Task create/update payload
    │   │   │   ├── TaskResponse.java         # Task response DTO
    │   │   │   ├── TaskStatusUpdateRequest.java # Status and actual hours update
    │   │   │   ├── ResourceRequest.java      # Asset registration payload
    │   │   │   ├── ResourceResponse.java     # Asset response DTO
    │   │   │   ├── AllocationRequest.java    # Resource booking payload
    │   │   │   ├── AllocationResponse.java   # Allocation response DTO
    │   │   │   └── DashboardStatsResponse.java # High-level dashboard KPIs
    │   │   ├── entity/
    │   │   │   ├── Role.java                 # ROLE_ADMIN, ROLE_MANAGER, ROLE_EMPLOYEE
    │   │   │   ├── Department.java           # Enterprise departments (Eng, IT, Prod, Ops)
    │   │   │   ├── User.java                 # User account entity
    │   │   │   ├── TaskPriority.java         # LOW, MEDIUM, HIGH, URGENT
    │   │   │   ├── TaskStatus.java           # TODO, IN_PROGRESS, IN_REVIEW, COMPLETED
    │   │   │   ├── Task.java                 # Task entity with estimations & assignees
    │   │   │   ├── ResourceType.java         # EQUIPMENT, SOFTWARE_LICENSE, SERVER, etc.
    │   │   │   ├── ResourceStatus.java       # AVAILABLE, ALLOCATED, MAINTENANCE, RETIRED
    │   │   │   ├── Resource.java             # Enterprise physical & digital assets
    │   │   │   ├── AllocationStatus.java     # ACTIVE, RETURNED, CANCELLED
    │   │   │   └── ResourceAllocation.java   # Resource-to-Task/User allocation mapping
    │   │   ├── exception/
    │   │   │   ├── BadRequestException.java
    │   │   │   ├── ResourceNotFoundException.java
    │   │   │   └── GlobalExceptionHandler.java # @RestControllerAdvice for uniform errors
    │   │   ├── repository/
    │   │   │   ├── DepartmentRepository.java
    │   │   │   ├── UserRepository.java
    │   │   │   ├── TaskRepository.java
    │   │   │   ├── ResourceRepository.java
    │   │   │   └── ResourceAllocationRepository.java
    │   │   └── service/
    │   │       ├── AuthService.java
    │   │       ├── UserService.java
    │   │       ├── TaskService.java
    │   │       ├── ResourceService.java
    │   │       ├── AllocationService.java
    │   │       └── DashboardService.java
    │   └── resources/
    │       ├── application.properties        # Main config (Active profile, JWT keys)
    │       ├── application-dev.properties    # H2 In-Memory DB (Zero-config instant launch)
    │       ├── application-mysql.properties  # MySQL Production Database connection
    │       └── static/                       # Served directly at http://localhost:8080/
    │           ├── index.html                # Single Page Enterprise UI
    │           ├── css/styles.css            # Enterprise Slate/Navy Design System
    │           └── js/
    │               ├── api.js                # ES6 Fetch wrapper with auto-Bearer injection
    │               ├── auth.js               # JWT Auth & RBAC state management
    │               ├── dashboard.js          # Live KPI metrics & charts
    │               ├── tasks.js              # Task board, CRUD, quick status picker
    │               ├── resources.js          # Asset inventory & capacity tracking
    │               ├── allocations.js        # Check-out, check-in, bookings
    │               └── app.js                # SPA navigation & view orchestrator
    └── test/
        └── java/com/enterprise/taskresource/
            └── TaskResourceManagementApplicationTests.java
```

---

## 🚀 How to Run the Project

### 1. Instant Launch (Default Dev Profile - In-Memory DB)
No database installation or password required to immediately test the system:

```powershell
cd C:\Users\yadav\.gemini\antigravity\scratch\enterprise-task-resource-mgmt
mvn spring-boot:run
```
The application will launch on `http://localhost:8080/`.

### 2. Running with MySQL Database

1. Open your MySQL client and ensure the schema exists:
```sql
CREATE DATABASE IF NOT EXISTS task_resource_db;
```
*(You can also execute `schema.sql` directly for predefined tables and foreign keys).*

2. Verify or update credentials in `src/main/resources/application-mysql.properties`:
```properties
spring.datasource.username=root
spring.datasource.password=root
```

3. Launch with the `mysql` active profile:
```powershell
mvn spring-boot:run -Dspring-boot.run.profiles=mysql
```

---

## 🔑 Pre-seeded Default Credentials

| Role | Username | Password | Access Privileges |
|------|----------|----------|-------------------|
| **Administrator** | `admin` | `Admin@123` | Full access across all tasks, resources, allocations & users |
| **Manager** | `manager` | `Manager@123` | Create/edit tasks, allocate resources, team oversight |
| **Employee** | `employee` | `Employee@123` | View assigned tasks, update status, check-in resources |

---

## 🌐 REST API Endpoints Reference

### Authentication
- `POST /api/auth/login` - Authenticate & obtain Bearer JWT token
- `POST /api/auth/register` - Create a new user account
- `GET /api/auth/me` - Get profile of current logged-in user

### Tasks
- `GET /api/tasks` - List all tasks (or employee-specific tasks)
- `GET /api/tasks/{id}` - Get task details
- `POST /api/tasks` - Create new task (*ADMIN, MANAGER*)
- `PUT /api/tasks/{id}` - Update task (*ADMIN, MANAGER*)
- `PATCH /api/tasks/{id}/status` - Quick update status & actual hours
- `DELETE /api/tasks/{id}` - Delete task (*ADMIN, MANAGER*)

### Resources & Assets
- `GET /api/resources` - List all enterprise resources
- `GET /api/resources/{id}` - Get resource by ID
- `POST /api/resources` - Register new resource (*ADMIN, MANAGER*)
- `PUT /api/resources/{id}` - Update resource details (*ADMIN, MANAGER*)
- `DELETE /api/resources/{id}` - Retire resource (*ADMIN*)

### Allocations & Bookings
- `GET /api/allocations` - List active & historical allocations
- `POST /api/allocations` - Allocate resource to user/task (*ADMIN, MANAGER*)
- `POST /api/allocations/{id}/return` - Return / check-in resource
- `POST /api/allocations/{id}/cancel` - Cancel allocation (*ADMIN, MANAGER*)

### Dashboard & Analytics
- `GET /api/dashboard/stats` - Live KPI statistics & utilization rates