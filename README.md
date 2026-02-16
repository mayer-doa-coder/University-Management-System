# University Management System

A Spring Boot REST API with role-based access control, PostgreSQL database, and Docker support.

---

## 🚀 Quick Start (3 Steps)

### Step 1: Clone & Navigate
```bash
cd D:\university-management-system
```

### Step 2: Start Everything with Docker
```bash
docker-compose up -d --build
```

### Step 3: Wait & Access
Wait ~30 seconds for startup, tashen open:
- **Frontend**: http://localhost:8080
- **API**: http://localhost:8080/api

### Default Login Credentials
| Username | Password | Role |
|----------|----------|------|
| `teacher` | `teacher123` | TEACHER (full access) |
| `student` | `student123` | STUDENT (limited access) |

---

## 📊 Connect to Database (pgAdmin 4)

If you have a **local PostgreSQL** installed, it uses port 5432. The Docker PostgreSQL uses port **5433**.

### pgAdmin Connection Settings:
| Field | Value |
|-------|-------|
| Host | `localhost` |
| Port | `5433` |
| Database | `mydatabase` |
| Username | `myuser` |
| Password | `secret` |

### View Tables:
Navigate to: **Servers → YourConnection → Databases → mydatabase → Schemas → public → Tables**

Tables: `students`, `teachers`, `courses`, `departments`, `users`, `student_courses`

---

## 🔧 Common Commands

### Start the application
```bash
docker-compose up -d --build
```

### Stop the application
```bash
docker-compose down
```

### View logs
```bash
docker logs university-management-system-app-1 --tail 50
```

### Reset database (delete all data)
```bash
docker-compose down -v
docker-compose up -d --build
```

### Rebuild after code changes
```bash
docker-compose down
docker-compose up -d --build
```

---

## 🔑 Role Permissions

| Action | STUDENT | TEACHER |
|--------|---------|---------|
| View all data | ✅ | ✅ |
| Update own info | ✅ (except roll number) | ✅ |
| Create/Delete Courses | ❌ | ✅ |
| Manage Students | ❌ | ✅ |
| Manage Teachers | ❌ | ✅ |
| Manage Departments | ❌ | ✅ |

---

## 📚 API Endpoints

### Base URL: `http://localhost:8080/api`

### Departments
```
GET    /departments      - Get all
GET    /departments/{id} - Get by ID
POST   /departments      - Create (TEACHER)
PUT    /departments/{id} - Update (TEACHER)
DELETE /departments/{id} - Delete (TEACHER)
```

### Courses
```
GET    /courses      - Get all
GET    /courses/{id} - Get by ID
POST   /courses      - Create (TEACHER)
PUT    /courses/{id} - Update (TEACHER)
DELETE /courses/{id} - Delete (TEACHER)
```

### Students
```
GET    /students           - Get all
GET    /students/{id}      - Get by ID
POST   /students           - Create (TEACHER)
PUT    /students/{id}      - Update (TEACHER)
PUT    /students/{id}/self - Update own info (STUDENT, no roll number change)
DELETE /students/{id}      - Delete (TEACHER)
```

### Teachers
```
GET    /teachers      - Get all
GET    /teachers/{id} - Get by ID
POST   /teachers      - Create (TEACHER)
PUT    /teachers/{id} - Update (TEACHER)
DELETE /teachers/{id} - Delete (TEACHER)
```

---

## 🧪 Test API with cURL

### Get all students (as teacher)
```bash
curl -u teacher:teacher123 http://localhost:8080/api/students
```

### Create a course (as teacher)
```bash
curl -X POST -u teacher:teacher123 -H "Content-Type: application/json" -d "{\"name\":\"Data Structures\",\"code\":\"CS102\",\"credits\":3}" http://localhost:8080/api/courses
```

### Student updates own info
```bash
curl -X PUT -u student:student123 -H "Content-Type: application/json" -d "{\"name\":\"Jane Updated\",\"email\":\"jane.new@uni.com\"}" http://localhost:8080/api/students/1/self
```

### Delete a course (teacher only)
```bash
curl -X DELETE -u teacher:teacher123 http://localhost:8080/api/courses/1
```

---

## 🧪 Test API with PowerShell

### Get all students
```powershell
$cred = [Convert]::ToBase64String([Text.Encoding]::ASCII.GetBytes("teacher:teacher123"))
Invoke-RestMethod -Uri "http://localhost:8080/api/students" -Headers @{Authorization="Basic $cred"}
```

### Get all courses
```powershell
$cred = [Convert]::ToBase64String([Text.Encoding]::ASCII.GetBytes("teacher:teacher123"))
Invoke-RestMethod -Uri "http://localhost:8080/api/courses" -Headers @{Authorization="Basic $cred"}
```

---

## 📁 Project Structure

```
university-management-system/
├── src/main/java/com/springproject/universitymanagementsystem/
│   ├── config/
│   │   ├── DataInitializer.java     # Creates sample data on startup
│   │   └── SecurityConfig.java      # Security & role configuration
│   ├── controller/
│   │   ├── CourseController.java
│   │   ├── DepartmentController.java
│   │   ├── StudentController.java
│   │   └── TeacherController.java
│   ├── entity/
│   │   ├── Course.java
│   │   ├── Department.java
│   │   ├── Student.java
│   │   ├── Teacher.java
│   │   └── User.java
│   ├── repository/
│   │   └── *Repository.java
│   ├── service/
│   │   └── *Service.java
│   └── UniversityManagementSystemApplication.java
├── src/main/resources/
│   ├── static/                      # Frontend HTML files
│   │   ├── index.html               # Login page
│   │   ├── dashboard.html
│   │   ├── students.html
│   │   ├── teachers.html
│   │   ├── courses.html
│   │   └── departments.html
│   └── application.properties
├── compose.yaml                     # Docker Compose config
├── Dockerfile
└── pom.xml
```

---

## 🗄️ Entity Relationships

```
┌─────────────┐     ┌─────────────┐     ┌─────────────┐
│  Department │◄────│   Student   │────►│   Course    │
└─────────────┘     └─────────────┘     └─────────────┘
       │                                       │
       │            ┌─────────────┐            │
       └───────────►│   Teacher   │◄───────────┘
                    └─────────────┘
```

- **Student → Department**: Many-to-One
- **Student ↔ Course**: Many-to-Many
- **Teacher → Department**: Many-to-One
- **Course → Teacher**: Many-to-One
- **Course → Department**: Many-to-One

---

## 🛠️ Technologies

- Java 17
- Spring Boot 3.2.2
- Spring Security (Basic Auth)
- Spring Data JPA
- PostgreSQL 15
- Docker & Docker Compose
- Lombok

---

## ❓ Troubleshooting

### Port 5432 already in use
You have a local PostgreSQL. The Docker PostgreSQL uses port **5433**. Update pgAdmin to use port 5433.

### Cannot connect to database
```bash
# Check if containers are running
docker ps

# Check app logs
docker logs university-management-system-app-1

# Restart everything
docker-compose down -v
docker-compose up -d --build
```

### Data not persisting after restart
Make sure you're using `docker-compose down` (without `-v`). The `-v` flag deletes the database volume.

### Login not working
Default credentials:
- Teacher: `teacher` / `teacher123`
- Student: `student` / `student123`
