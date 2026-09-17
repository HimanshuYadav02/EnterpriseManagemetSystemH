# Enterprise Task & Resource Management System

A full-stack enterprise application for managing **users, departments, tasks, resources, allocations, and dashboard analytics** through a secure and responsive web interface.

## 🔗 Live Demo

- **Frontend:** `https://your-frontend-live-url.com`
- **Backend API:** `https://your-backend-live-url.com`
- **API Base URL:** `https://your-backend-live-url.com/api`

> Replace the placeholder URLs above with your deployed frontend and backend links.

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

### 🧰 Resource Management
- Register enterprise resources
- Track resource type and status
- Monitor available and allocated resources
- Manage physical and digital assets
- Resource inventory and capacity tracking

### 📅 Resource Allocation
- Book resources for users or tasks
- Check-out and check-in resources
- Track allocation history
- Manage active, returned, and cancelled allocations

### 🏢 Department & User Management
- Manage departments
- View user profiles
- Assign users to departments
- Manage user roles and permissions

## 🛠️ Technology Stack

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
enterprise-task-resource-mgmt/
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

### 1. Clone the repository

```bash
git clone https://github.com/YOUR-USERNAME/YOUR-REPOSITORY.git
cd enterprise-task-resource-mgmt
```

### 2. Configure the database

Create a MySQL database:

```sql
CREATE DATABASE task_resource_db;
```

Update your MySQL configuration in:

```text
src/main/resources/application-mysql.properties
```

Example:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/task_resource_db
spring.datasource.username=YOUR_DB_USERNAME
spring.datasource.password=YOUR_DB_PASSWORD
```

> Do not commit real passwords, JWT secrets, or private credentials to GitHub.

### 3. Run the application

For development:

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

For MySQL:

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=mysql
```

The application will be available at:

```text
http://localhost:8080
```

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

> Endpoint names may vary depending on the final controller mappings in the project.

## 🧪 Testing

Run the test suite with:

```bash
mvn test
```

Build the project with:

```bash
mvn clean package
```

## 🚀 Deployment

### Backend Deployment
The Spring Boot backend can be deployed using platforms such as:

- Render
- Railway
- AWS
- Azure
- Google Cloud

Build command:

```bash
mvn clean package
```

Run command:

```bash
java -jar target/*.jar
```

### Frontend Deployment
The frontend is served from Spring Boot's static resources directory:

```text
src/main/resources/static/
```

After deployment, update the frontend API base URL in:

```text
src/main/resources/static/js/api.js
```

Example:

```javascript
const API_BASE_URL = "https://your-backend-live-url.com/api";
```

## 🔒 Security Notes

- Passwords are handled through Spring Security.
- JWT tokens are used for authenticated requests.
- CORS configuration is included in the backend.
- Never expose database passwords or JWT secrets publicly.
- Use environment variables for production credentials.
- Configure HTTPS for production deployment.

## 📸 Screenshots

Add screenshots of the following pages to showcase the application:

1. Login page
2. Registration page
3. Dashboard with charts
4. Task management page
5. Resource inventory page
6. Resource allocation page

Example:

```markdown
![Dashboard Screenshot](screenshots/dashboard.png)
```

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

- GitHub: `https://github.com/YOUR-USERNAME`
- LinkedIn: `https://www.linkedin.com/in/YOUR-PROFILE`

## 📄 License

This project is intended for educational and demonstration purposes. Add your preferred license here if required.

