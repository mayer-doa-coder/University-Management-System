# University Management System - Project Workflow Documentation

## Table of Contents
1. [Project Overview](#project-overview)
2. [Architecture Overview](#architecture-overview)
3. [Directory Structure](#directory-structure)
4. [Authentication & Authorization](#authentication--authorization)
5. [Spring Security Implementation](#spring-security-implementation)
6. [Database Configuration](#database-configuration)
7. [REST API Implementation](#rest-api-implementation)
8. [MVC Architecture](#mvc-architecture)
9. [Frontend Implementation](#frontend-implementation)
10. [Data Flow Diagram](#data-flow-diagram)

---

## Project Overview

This is a **Spring Boot 3.2.2** application that provides a University Management System with role-based access control for Teachers and Students. The system manages:
- Departments
- Teachers
- Students
- Courses

**Tech Stack:**
- **Backend:** Java 17, Spring Boot 3.2.2
- **Security:** Spring Security 6
- **Database:** PostgreSQL with JPA/Hibernate
- **Frontend:** HTML, CSS, JavaScript (vanilla)
- **Build Tool:** Maven

---

## Architecture Overview

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                              CLIENT (Browser)                                │
│                    HTML/CSS/JavaScript (Static Files)                        │
└─────────────────────────────────────────────────────────────────────────────┘
                                      │
                                      │ HTTP Requests (REST API)
                                      │ Basic Authentication
                                      ▼
┌─────────────────────────────────────────────────────────────────────────────┐
│                           SPRING SECURITY FILTER                             │
│                         (SecurityFilterChain)                                │
│                    📁 config/SecurityConfig.java                             │
└─────────────────────────────────────────────────────────────────────────────┘
                                      │
                                      │ Authorization Check
                                      ▼
┌─────────────────────────────────────────────────────────────────────────────┐
│                          CONTROLLER LAYER (REST)                             │
│              📁 controller/*.java (AuthController, StudentController, etc.)  │
└─────────────────────────────────────────────────────────────────────────────┘
                                      │
                                      │ Business Logic Delegation
                                      ▼
┌─────────────────────────────────────────────────────────────────────────────┐
│                            SERVICE LAYER                                     │
│       📁 service/*.java (StudentService, CourseService, TeacherService, etc.)│
└─────────────────────────────────────────────────────────────────────────────┘
                                      │
                                      │ Data Access
                                      ▼
┌─────────────────────────────────────────────────────────────────────────────┐
│                          REPOSITORY LAYER (JPA)                              │
│     📁 repository/*.java (StudentRepository, CourseRepository, etc.)         │
└─────────────────────────────────────────────────────────────────────────────┘
                                      │
                                      │ SQL Queries (Hibernate)
                                      ▼
┌─────────────────────────────────────────────────────────────────────────────┐
│                          POSTGRESQL DATABASE                                 │
│                        (localhost:5432/mydatabase)                           │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## Directory Structure

```
university-management-system/
├── src/main/java/com/springproject/universitymanagementsystem/
│   ├── UniversityManagementSystemApplication.java  # Main entry point
│   ├── config/
│   │   ├── SecurityConfig.java          # Spring Security configuration
│   │   └── DataInitializer.java         # Initial data seeding
│   ├── controller/
│   │   ├── AuthController.java          # Authentication endpoints
│   │   ├── CourseController.java        # Course REST API
│   │   ├── DepartmentController.java    # Department REST API
│   │   ├── StudentController.java       # Student REST API
│   │   └── TeacherController.java       # Teacher REST API
│   ├── entity/
│   │   ├── Course.java                  # Course JPA entity
│   │   ├── Department.java              # Department JPA entity
│   │   ├── Student.java                 # Student JPA entity
│   │   ├── Teacher.java                 # Teacher JPA entity
│   │   └── User.java                    # User JPA entity (authentication)
│   ├── repository/
│   │   ├── CourseRepository.java        # Course data access
│   │   ├── DepartmentRepository.java    # Department data access
│   │   ├── StudentRepository.java       # Student data access
│   │   ├── TeacherRepository.java       # Teacher data access
│   │   └── UserRepository.java          # User data access
│   └── service/
│       ├── CourseService.java           # Course business logic
│       ├── CustomUserDetailsService.java # Spring Security user loading
│       ├── DepartmentService.java       # Department business logic
│       ├── StudentService.java          # Student business logic
│       └── TeacherService.java          # Teacher business logic
├── src/main/resources/
│   ├── application.properties           # Database & app configuration
│   └── static/
│       ├── index.html                   # Login page
│       ├── dashboard.html               # Main dashboard
│       ├── students.html                # Student management
│       ├── teachers.html                # Teacher management
│       ├── courses.html                 # Course management
│       ├── departments.html             # Department management
│       ├── css/
│       │   ├── style.css                # Main stylesheet
│       │   └── uni.css                  # Additional styles
│       └── js/
│           └── app.js                   # Frontend JavaScript
└── pom.xml                              # Maven dependencies
```

---

## Authentication & Authorization

### Where Authentication is Handled

| Component | File Location | Purpose |
|-----------|--------------|---------|
| Security Configuration | `config/SecurityConfig.java` | Defines security rules, password encoder, authentication manager |
| User Details Service | `service/CustomUserDetailsService.java` | Loads user from database for Spring Security |
| Auth Controller | `controller/AuthController.java` | Handles `/api/auth/*` endpoints (register, login) |
| User Entity | `entity/User.java` | Stores user credentials and role |
| User Repository | `repository/UserRepository.java` | Database access for user data |

### Authentication Flow

```
1. User enters credentials on login page (index.html)
                    │
                    ▼
2. JavaScript (app.js) creates Base64 encoded credentials
   and stores in sessionStorage
                    │
                    ▼
3. All API requests include "Authorization: Basic <credentials>" header
                    │
                    ▼
4. SecurityFilterChain intercepts request
                    │
                    ▼
5. CustomUserDetailsService.loadUserByUsername() loads user from DB
                    │
                    ▼
6. Spring Security validates password using BCryptPasswordEncoder
                    │
                    ▼
7. If valid, request proceeds; if invalid, 401 Unauthorized returned
```

### Authorization (Role-Based Access Control)

**Roles defined in `User.java`:**
```java
public enum Role {
    STUDENT, TEACHER
}
```

**Authorization Rules (defined in `SecurityConfig.java`):**

| Endpoint | Method | STUDENT | TEACHER |
|----------|--------|---------|---------|
| `/api/auth/**` | ALL | ✅ Public | ✅ Public |
| `/api/**` | GET | ✅ | ✅ |
| `/api/courses/**` | DELETE | ❌ | ✅ |
| `/api/students/**` | POST | ❌ | ✅ |
| `/api/students/**` | DELETE | ❌ | ✅ |
| `/api/students/{id}/self` | PUT | ✅ (own data) | ❌ |
| `/api/**` | POST/PUT/DELETE | ❌ | ✅ |

---

## Spring Security Implementation

### File: `config/SecurityConfig.java`

```
Key Components:
├── @EnableWebSecurity          → Enables Spring Security
├── @EnableMethodSecurity       → Enables @PreAuthorize annotations
├── PasswordEncoder Bean        → BCryptPasswordEncoder for password hashing
├── AuthenticationManager Bean  → Manages authentication process
└── SecurityFilterChain Bean    → Defines URL-based security rules
```

**Security Configuration Details:**

| Feature | Implementation |
|---------|---------------|
| CSRF Protection | Disabled (for REST API statelessness) |
| Session Management | STATELESS (no server-side sessions) |
| Authentication Method | HTTP Basic Authentication |
| Password Encoding | BCrypt (industry standard) |
| Method Security | `@PreAuthorize` annotations on controllers |

### File: `service/CustomUserDetailsService.java`

This service implements `UserDetailsService` interface:

```java
@Override
public UserDetails loadUserByUsername(String username) {
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("User not found"));

    return new org.springframework.security.core.userdetails.User(
        user.getUsername(),
        user.getPassword(),
        Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()))
    );
}
```

---

## Database Configuration

### File: `application.properties`

```properties
# PostgreSQL Connection
spring.datasource.url=jdbc:postgresql://localhost:5432/mydatabase
spring.datasource.username=myuser
spring.datasource.password=secret
spring.datasource.driver-class-name=org.postgresql.Driver

# JPA/Hibernate Configuration
spring.jpa.hibernate.ddl-auto=update    # Auto-creates/updates tables
spring.jpa.show-sql=true                # Logs SQL queries
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
```

### Entity-Relationship Diagram

```
┌──────────────┐       ┌──────────────┐       ┌──────────────┐
│    User      │       │  Department  │       │   Course     │
├──────────────┤       ├──────────────┤       ├──────────────┤
│ id (PK)      │       │ id (PK)      │       │ id (PK)      │
│ username     │       │ name         │       │ name         │
│ password     │       │ description  │       │ code         │
│ role         │       └──────┬───────┘       │ credits      │
│ student_id(FK)│              │              │ department_id│
│ teacher_id(FK)│              │              │ teacher_id   │
└──────────────┘              │              └──────────────┘
                              │
        ┌─────────────────────┼─────────────────────┐
        │                     │                     │
        ▼                     ▼                     ▼
┌──────────────┐       ┌──────────────┐       ┌──────────────┐
│   Student    │       │   Teacher    │       │student_courses│
├──────────────┤       ├──────────────┤       ├──────────────┤
│ id (PK)      │       │ id (PK)      │       │ student_id   │
│ rollNumber   │       │ name         │       │ course_id    │
│ name         │       │ email        │       └──────────────┘
│ email        │       │ phone        │       (Many-to-Many)
│ phone        │       │ specialization│
│ department_id│       │ department_id│
└──────────────┘       └──────────────┘
```

### Data Initialization

**File: `config/DataInitializer.java`**

On application startup, if no users exist:
1. Creates a Computer Science department
2. Creates a sample teacher (Dr. John Smith)
3. Creates a sample student (Jane Doe)
4. Creates a sample course (Introduction to Programming)
5. Creates user accounts with encrypted passwords

**Default Credentials:**
- **Teacher:** `teacher` / `teacher123`
- **Student:** `student` / `student123`

---

## REST API Implementation

### API Endpoints

| Controller | Base URL | Endpoints |
|------------|----------|-----------|
| AuthController | `/api/auth` | POST `/register`, GET `/login` |
| StudentController | `/api/students` | GET, POST, PUT, DELETE |
| TeacherController | `/api/teachers` | GET, POST, PUT, DELETE |
| CourseController | `/api/courses` | GET, POST, PUT, DELETE |
| DepartmentController | `/api/departments` | GET, POST, PUT, DELETE |

### REST API Pattern Used

```java
@RestController
@RequestMapping("/api/students")
public class StudentController {

    @GetMapping              // GET /api/students - List all
    @GetMapping("/{id}")     // GET /api/students/1 - Get by ID
    @PostMapping             // POST /api/students - Create new
    @PutMapping("/{id}")     // PUT /api/students/1 - Update
    @DeleteMapping("/{id}")  // DELETE /api/students/1 - Delete
}
```

### Response Format

All API responses return JSON:

```json
// Success - Single Entity
{
    "id": 1,
    "rollNumber": "CS2024001",
    "name": "Jane Doe",
    "email": "jane.doe@university.com",
    "phone": "0987654321"
}

// Success - List
[
    { "id": 1, "name": "..." },
    { "id": 2, "name": "..." }
]

// Error
{
    "error": "User not found"
}
```

---

## MVC Architecture

This project follows a **hybrid MVC + REST API** architecture:

### Traditional MVC Components

| Component | Location | Responsibility |
|-----------|----------|----------------|
| **Model** | `entity/*.java` | Data structures (JPA entities) |
| **View** | `static/*.html` | UI templates (static HTML) |
| **Controller** | `controller/*.java` | Handle HTTP requests |

### Layered Architecture Pattern

```
┌────────────────────────────────────────────────────────────┐
│                    PRESENTATION LAYER                       │
│  ┌──────────────────┐    ┌──────────────────┐              │
│  │  Static HTML     │    │  REST Controllers │              │
│  │  (View)          │◄───│  (@RestController)│              │
│  └──────────────────┘    └──────────────────┘              │
│         │                        │                          │
│         │  JavaScript            │ JSON                     │
│         │  API Calls             │ Responses                │
│         └────────────────────────┘                          │
└────────────────────────────────────────────────────────────┘
                          │
                          ▼
┌────────────────────────────────────────────────────────────┐
│                    BUSINESS LOGIC LAYER                     │
│  ┌──────────────────────────────────────────────────────┐  │
│  │                  Service Classes                       │  │
│  │  StudentService, CourseService, TeacherService, etc.  │  │
│  │  - Validation logic                                    │  │
│  │  - Business rules                                      │  │
│  │  - Transaction management                              │  │
│  └──────────────────────────────────────────────────────┘  │
└────────────────────────────────────────────────────────────┘
                          │
                          ▼
┌────────────────────────────────────────────────────────────┐
│                    DATA ACCESS LAYER                        │
│  ┌──────────────────────────────────────────────────────┐  │
│  │                 Repository Interfaces                  │  │
│  │  extends JpaRepository<Entity, Long>                  │  │
│  │  - CRUD operations (provided by Spring Data JPA)      │  │
│  │  - Custom query methods                               │  │
│  └──────────────────────────────────────────────────────┘  │
└────────────────────────────────────────────────────────────┘
                          │
                          ▼
┌────────────────────────────────────────────────────────────┐
│                    PERSISTENCE LAYER                        │
│  ┌──────────────────────────────────────────────────────┐  │
│  │                   Entity Classes                       │  │
│  │  @Entity - JPA/Hibernate managed                      │  │
│  │  Maps to database tables                              │  │
│  └──────────────────────────────────────────────────────┘  │
└────────────────────────────────────────────────────────────┘
```

---

## Frontend Implementation

### Static File Location

All frontend files are in `src/main/resources/static/`:

| File | Purpose |
|------|---------|
| `index.html` | Login page |
| `dashboard.html` | Main dashboard after login |
| `students.html` | Student management CRUD |
| `teachers.html` | Teacher management CRUD |
| `courses.html` | Course management CRUD |
| `departments.html` | Department management CRUD |
| `css/style.css` | Main stylesheet |
| `js/app.js` | JavaScript application logic |

### Frontend Authentication Flow

**File: `js/app.js`**

```javascript
// 1. Store credentials on login
sessionStorage.setItem('authCredentials', btoa(username + ':' + password));
sessionStorage.setItem('username', username);
sessionStorage.setItem('role', role);

// 2. Include credentials in all API requests
function getAuthHeader() {
    const credentials = sessionStorage.getItem('authCredentials');
    return 'Basic ' + credentials;
}

// 3. Make authenticated API requests
async function apiRequest(url, method = 'GET', body = null) {
    const options = {
        method: method,
        headers: {
            'Authorization': getAuthHeader(),
            'Content-Type': 'application/json'
        }
    };
    // ...
}
```

### Static Files Serving

Spring Boot automatically serves files from `src/main/resources/static/` at the root URL:
- `http://localhost:8080/` → `static/index.html`
- `http://localhost:8080/dashboard.html` → `static/dashboard.html`
- `http://localhost:8080/css/style.css` → `static/css/style.css`

---

## Data Flow Diagram

### Complete Request Flow Example: Creating a Student

```
┌─────────────────────────────────────────────────────────────────────────┐
│ 1. USER ACTION                                                           │
│    Teacher fills form and clicks "Add Student" on students.html          │
└───────────────────────────────────┬─────────────────────────────────────┘
                                    │
                                    ▼
┌─────────────────────────────────────────────────────────────────────────┐
│ 2. JAVASCRIPT (app.js)                                                   │
│    apiRequest('/api/students', 'POST', studentData)                      │
│    Headers: { Authorization: 'Basic dGVhY2hlcjp0ZWFjaGVyMTIz' }         │
└───────────────────────────────────┬─────────────────────────────────────┘
                                    │
                                    ▼
┌─────────────────────────────────────────────────────────────────────────┐
│ 3. SPRING SECURITY (SecurityFilterChain)                                 │
│    - Validates Basic Auth credentials                                    │
│    - Loads user via CustomUserDetailsService                            │
│    - Checks role (TEACHER required for POST /api/students)              │
└───────────────────────────────────┬─────────────────────────────────────┘
                                    │
                                    ▼
┌─────────────────────────────────────────────────────────────────────────┐
│ 4. CONTROLLER (StudentController.java)                                   │
│    @PostMapping                                                          │
│    @PreAuthorize("hasRole('TEACHER')")                                  │
│    public Student create(@RequestBody Student student) {                 │
│        return studentService.save(student);                              │
│    }                                                                     │
└───────────────────────────────────┬─────────────────────────────────────┘
                                    │
                                    ▼
┌─────────────────────────────────────────────────────────────────────────┐
│ 5. SERVICE (StudentService.java)                                         │
│    public Student save(Student student) {                                │
│        return studentRepository.save(student);                           │
│    }                                                                     │
└───────────────────────────────────┬─────────────────────────────────────┘
                                    │
                                    ▼
┌─────────────────────────────────────────────────────────────────────────┐
│ 6. REPOSITORY (StudentRepository.java)                                   │
│    extends JpaRepository<Student, Long>                                  │
│    save() method provided by Spring Data JPA                            │
└───────────────────────────────────┬─────────────────────────────────────┘
                                    │
                                    ▼
┌─────────────────────────────────────────────────────────────────────────┐
│ 7. DATABASE (PostgreSQL)                                                 │
│    INSERT INTO students (roll_number, name, email, phone, department_id) │
│    VALUES ('CS2024002', 'John Doe', 'john@uni.com', '123456', 1)        │
└───────────────────────────────────┬─────────────────────────────────────┘
                                    │
                                    ▼
┌─────────────────────────────────────────────────────────────────────────┐
│ 8. RESPONSE                                                              │
│    JSON returned: { "id": 2, "rollNumber": "CS2024002", ... }           │
│    JavaScript updates the UI table                                       │
└─────────────────────────────────────────────────────────────────────────┘
```

---

## Quick Reference

### Key Files by Function

| Function | Primary File(s) |
|----------|-----------------|
| Security Rules | `config/SecurityConfig.java` |
| User Authentication | `service/CustomUserDetailsService.java` |
| Initial Data | `config/DataInitializer.java` |
| Database Config | `application.properties` |
| REST Endpoints | `controller/*.java` |
| Business Logic | `service/*.java` |
| Data Access | `repository/*.java` |
| Data Models | `entity/*.java` |
| Frontend UI | `static/*.html` |
| Frontend Logic | `static/js/app.js` |
| Styling | `static/css/style.css` |

### Default Credentials

| Role | Username | Password |
|------|----------|----------|
| Teacher | `teacher` | `teacher123` |
| Student | `student` | `student123` |

### API Base URL

```
http://localhost:8080/api/
```

### Access Application

```
http://localhost:8080/
```

---

## Summary

This University Management System demonstrates a well-structured Spring Boot application with:

1. **Clear Separation of Concerns** - Controller → Service → Repository layers
2. **Secure Authentication** - Spring Security with BCrypt password hashing
3. **Role-Based Authorization** - Teacher/Student roles with different permissions
4. **RESTful API Design** - Standard HTTP methods and JSON responses
5. **Stateless Architecture** - No server-side sessions, token-based auth
6. **JPA/Hibernate ORM** - Clean database abstraction
7. **Static Frontend** - JavaScript SPA communicating via REST API
