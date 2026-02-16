# 🎓 How to Demonstrate Tests to Your Professor

## Quick Commands to Run

### 1️⃣ Run All Tests (Recommended)
```powershell
cd D:\university-management-system
.\mvnw.cmd clean test
```

### 2️⃣ Expected Output
You should see at the end:
```
[INFO] Tests run: 135, Failures: 0, Errors: 0, Skipped: 0
[INFO] 
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
```

---

## 📋 What to Show Your Professor

### A. Individual Test Reports
After running tests, open these files in the `target/surefire-reports/` folder:

| File | Contents |
|------|----------|
| `CourseControllerIntegrationTest.txt` | 13 tests - API endpoint tests |
| `StudentControllerIntegrationTest.txt` | 14 tests - Student API tests |
| `StudentControllerTest.txt` | 6 tests - Controller unit tests |
| `CourseTest.txt` | 28 tests - Course entity tests |
| `StudentTest.txt` | 22 tests - Student entity tests |
| `CourseRepositoryTest.txt` | 14 tests - Database operation tests |
| `StudentRepositoryTest.txt` | 13 tests - Student DB tests |
| `CourseServiceTest.txt` | 11 tests - Service layer tests |
| `StudentServiceTest.txt` | 13 tests - Student service tests |
| `UniversityManagementSystemApplicationTests.txt` | 1 test - Context loads |

### B. Test Source Code Location
```
src/test/java/com/springproject/universitymanagementsystem/
├── controller/
│   ├── CourseControllerIntegrationTest.java   ← REST API tests with MockMvc
│   ├── StudentControllerIntegrationTest.java  ← Student API tests
│   └── StudentControllerTest.java             ← Controller unit tests
├── entity/
│   ├── CourseTest.java                        ← Course entity tests
│   └── StudentTest.java                       ← Student entity tests
├── repository/
│   ├── CourseRepositoryTest.java              ← JPA repository tests
│   └── StudentRepositoryTest.java             ← Student repository tests
├── service/
│   ├── CourseServiceTest.java                 ← Business logic tests
│   └── StudentServiceTest.java                ← Student service tests
└── UniversityManagementSystemApplicationTests.java ← Context loads test
```

---

## 🧪 Key Test Types to Highlight

### 1. Integration Tests (with @SpringBootTest)
- Test full request/response cycle
- Uses MockMvc for HTTP requests
- Tests role-based security (STUDENT/TEACHER roles)

### 2. Repository Tests (with @DataJpaTest)
- Uses H2 in-memory database
- Tests CRUD operations
- Tests custom queries

### 3. Service Tests (with @ExtendWith(MockitoExtension.class))
- Uses Mockito for mocking dependencies
- Tests business logic in isolation

### 4. Entity Tests
- Tests getters, setters, constructors
- Tests entity relationships

---

## 📊 Test Summary Table

| Layer | Test Classes | Total Tests |
|-------|--------------|-------------|
| Controller | 3 | 33 |
| Entity | 2 | 50 |
| Repository | 2 | 27 |
| Service | 2 | 24 |
| Application | 1 | 1 |
| **TOTAL** | **10** | **135** |

---

## 🏃 Running with Docker (Optional)

If Docker is available:
```powershell
cd D:\university-management-system
docker-compose up -d --build
```

Then test the API:
```powershell
# Test the API
curl http://localhost:8080/api/courses
```

---

## ✅ Success Criteria

All tests should show:
- **Tests run: 135**
- **Failures: 0**
- **Errors: 0**
- **Skipped: 0**
- **BUILD SUCCESS**
