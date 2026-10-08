# Enterprise Task & Resource Management System

A full-stack enterprise application for managing **users, departments, tasks, resources, allocations, and dashboard analytics** through a secure and responsive web interface.

## 🔗 Live Demo

- **Live Demo:** https://enterprisemanagemetsystemh-4.onrender.com/
- **Backend API:** https://enterprisemanagemetsystemh-4.onrender.com/api
- **API Base URL:** https://enterprisemanagemetsystemh-4.onrender.com/api



## ✨ Features

### 🔐 Authentication & Authorization
- User registration and login
- JWT-based authentication
- Role-based access control
- Protected API routes
- Support for Admin, Manager, and Employee roles

### 📊 Dashboard
- Live KPI cards
- Task status overview
- Resource availability summary
- Allocation statistics
- Department-level insights
- Interactive charts and analytics

### ✅ Task Management
- Create, update, and view tasks
- Assign tasks to users
- Set task priority:
  - LOW
  - MEDIUM
  - HIGH
  - URGENT
- Track task status:
  - TODO
  - IN_PROGRESS
  - IN_REVIEW
  - COMPLETED
- Update actual hours and task progress

### Resource Management
- Register enterprise resources
- Track resource type and status
- Monitor available and allocated resources
- Manage physical and digital assets
- Resource inventory and capacity tracking

###  Resource Allocation
- Book resources for users or tasks
- Check-out and check-in resources
- Track allocation history
- Manage active, returned, and cancelled allocations

### 🏢 Department & User Management
- Manage departments
- View user profiles
- Assign users to departments
- Manage user roles and permissions

##  Technology Stack

### Backend
- Java
- Spring Boot
- Spring Security 6
- JWT Authentication
- Spring Data JPA
- Hibernate
- MySQL
- Maven

### Frontend
- HTML5
- CSS3
- JavaScript (ES6+)
- Fetch API
- Chart-based dashboard
- Single Page Application (SPA) navigation

### Database
- MySQL for production
- H2 in-memory database for development/testing

## 📁 Project Structure

```text
enterprise-task-resource
├── pom.xml
├── schema.sql
├── README.md
├── src/
│   ├── main/
│   │   ├── java/com/enterprise/taskresource/
│   │   │   ├── TaskResourceManagementApplication.java
│   │   │   ├── config/
│   │   │   │   ├── SecurityConfig.java
│   │   │   │   ├── JwtAuthFilter.java
│   │   │   │   ├── JwtUtils.java
│   │   │   │   ├── UserDetailsServiceImpl.java
│   │   │   │   └── DataInitializer.java
│   │   │   ├── controller/
│   │   │   ├── dto/
│   │   │   ├── entity/
│   │   │   ├── exception/
│   │   │   ├── repository/
│   │   │   └── service/
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── application-dev.properties
│   │       ├── application-mysql.properties
│   │       └── static/
│   │           ├── index.html
│   │           ├── css/
│   │           │   └── styles.css
│   │           └── js/
│   │               ├── api.js
│   │               ├── auth.js
│   │               ├── dashboard.js
│   │               ├── tasks.js
│   │               ├── resources.js
│   │               ├── allocations.js
│   │               └── app.js
│   └── test/
│       └── java/com/enterprise/taskresource/
│           └── TaskResourceManagementApplicationTests.java
```

## ⚙️ Local Setup

### Prerequisites

Install the following tools:

- Java 17 or later
- Maven 3.8+
- MySQL 8+ (for production-style setup)
- Git

## 🔌 API Endpoints

| Module | Endpoint | Method |
|---|---|---|
| Authentication | `/api/auth/register` | POST |
| Authentication | `/api/auth/login` | POST |
| Users | `/api/users` | GET |
| Departments | `/api/users/departments` | GET |
| Tasks | `/api/tasks` | GET, POST |
| Tasks | `/api/tasks/{id}` | PUT, DELETE |
| Resources | `/api/resources` | GET, POST |
| Allocations | `/api/allocations` | GET, POST |
| Dashboard | `/api/dashboard/stats` | GET |


## 📌 Roadmap

- [x] User authentication
- [x] JWT security
- [x] Task management
- [x] Resource management
- [x] Resource allocation
- [x] Dashboard statistics
- [ ] Email notifications
- [ ] Advanced reporting
- [ ] Export reports to PDF/Excel
- [ ] Docker deployment
- [ ] Automated CI/CD pipeline

## 👨‍💻 Author

**Himanshu Yadav**

- GitHub: `https://github.com/HimanshuYadav02`
- LinkedIn: `www.linkedin.com/in/
himanshu-yadav-0h20y
`

## 📄 License

This project is intended for educational and demonstration purposes.

