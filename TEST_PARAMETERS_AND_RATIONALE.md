# Test parameters & rationale — per test file 🔬

This document lists the *important parameters/inputs/annotations* used by each test file and **why** we test that file. Use this as a quick reference when reviewing, running, or extending tests.

---

## Summary (quick reference)
- Unit tests: business logic & small, fast verifications (Mockito / plain JUnit)
- Integration tests: full HTTP/API, Spring context, security and serialization
- Repository tests: JPA behavior, schema, transactions using H2 (@DataJpaTest)
- Entity tests: domain model invariants, getters/setters, relationships

---

## `src/test/java/.../service/StudentServiceTest.java` (Unit)
### Important parameters
- Annotations: `@ExtendWith(MockitoExtension.class)`
- Mocks: `StudentRepository` (mock behavior via `when(...)`)
- Injected SUT: `@InjectMocks StudentService`
- Argument matchers: `any()`, `anyLong()`
- Typical inputs: `id` (Long), `rollNumber` (String), `Student` objects with fields (name, email, phone, department)
- Assertions: AssertJ (`assertThat(...)`) + Mockito `verify(...)`

### Why tested
- Verify business logic in isolation (CRUD wrappers, edge cases: not-found, nulls)
- Ensure correct repository interactions and method call counts
- Prevent regressions in service-layer behavior

---

## `src/test/java/.../service/CourseServiceTest.java` (Unit)
### Important parameters
- Annotations: `@ExtendWith(MockitoExtension.class)`
- Mocks: `CourseRepository`
- Inputs: `Course` DTO/entity fields (`id`, `name`, `code`, `credits`)
- Behavior checks: `when(...).thenReturn(...)`, `verify(repo, times(n))`

### Why tested
- Validate service business rules (save/update/find/delete)
- Protect service contract and repository calls

---

## `src/test/java/.../repository/StudentRepositoryTest.java` (@DataJpaTest)
### Important parameters
- Annotation: `@DataJpaTest`, `@ActiveProfiles("test")`
- Test helper: `TestEntityManager` to persist/setup data
- DB: H2 in-memory (configured via `application-test.yml`)
- Typical inputs: entities with relations (Department → Student)
- Operations: `save()`, `findById()`, custom queries (e.g. `findByRollNumber`), `flush()` to force SQL
- Assertions: persisted id presence, field values, relationship integrity

### Why tested
- Verify JPA mappings, cascade behavior, repository queries and transactions
- Ensure repository queries return expected results against an actual DB (H2)

---

## `src/test/java/.../repository/CourseRepositoryTest.java` (@DataJpaTest)
### Important parameters
- Annotation: `@DataJpaTest`, `@ActiveProfiles("test")`
- Uses `TestEntityManager` and H2
- Inputs: `Course`, `Teacher`, `Department` entities (fields like `code`, `credits`)
- Operations: CRUD + custom repository queries

### Why tested
- Validate entity mappings and repository query correctness at DB level
- Prevent regression in persistence-layer behavior

---

## `src/test/java/.../entity/StudentTest.java` (Unit — entity)
### Important parameters
- Plain JUnit + AssertJ
- Inputs: model fields (`id`, `rollNumber`, `name`, `email`, `phone`), lists (`courses`), linked `User`
- Scenarios: getters/setters, constructors, null handling, relationship integrity

### Why tested
- Ensure domain model correctness and invariants (no accidental regressions in POJOs)
- Validate relationships and defensive handling of null/empty values

---

## `src/test/java/.../entity/CourseTest.java` (Unit — entity)
### Important parameters
- Tests getters/setters, constructors, null value behaviour for `code`/`credits`
- Inputs: `id`, `name`, `code`, `credits`, `department`, `teacher`, `students` list

### Why tested
- Protect the domain model (serializability, equality expectations, basic validation)

---

## `src/test/java/.../controller/StudentControllerIntegrationTest.java` (Integration)
### Important parameters
- Annotations: `@SpringBootTest`, `@AutoConfigureMockMvc`, `@ActiveProfiles("test")`
- HTTP client: `MockMvc` + `ObjectMapper` for JSON serialization
- Security helpers: `@WithMockUser(roles = "STUDENT" | "TEACHER")`
- MockBean: `@MockBean StudentService` (controller isolated from real service implementation)
- Requests tested: `GET /api/students`, `POST /api/students`, `PUT /api/students/{id}/self`, `DELETE /api/students/{id}`
- Checks: HTTP status codes (200, 201, 204, 401, 403, 404), JSON structure via `jsonPath()`, role-based access control

### Why tested
- Validate API contract (endpoints, status codes, response payloads)
- Ensure security rules (roles) and validation behavior are enforced at controller layer
- Good smoke-test for request → controller → (mocked) service flow

---

## `src/test/java/.../controller/CourseControllerIntegrationTest.java` (Integration)
### Important parameters
- Same stack as Student controller tests (`@SpringBootTest`, `@AutoConfigureMockMvc`)
- Uses `@WithMockUser` roles to verify access control
- Tests JSON serialization, request bodies, and response validation for course endpoints

### Why tested
- Ensure REST endpoints behave correctly under authentication and return expected JSON contract

---

## `src/test/java/.../controller/StudentControllerTest.java` (Unit / placeholder)
### Important parameters
- Plain JUnit test class (currently contains placeholder tests)

### Why tested
- Intended for lightweight controller-unit tests without starting Spring context
- Acts as scaffold for future focused unit tests (no-HTTP, direct method calls)

---

## `src/test/java/com/springproject/universitymanagementsystem/UniversityManagementSystemApplicationTests.java`
### Important parameters
- `@SpringBootTest` context-load test

### Why tested
- Sanity check that Spring application context starts successfully (smoke test)

---

## Common testing parameters & conventions used across files
- Profiles: `@ActiveProfiles("test")` → uses H2 and test-specific properties
- Assertions: AssertJ (`assertThat(...)`) for readable assertions
- Mocking: Mockito (`@Mock`, `@MockBean`, `@InjectMocks`, `when()`, `verify()`)
- JSON tests: `ObjectMapper` + `jsonPath()` checks
- Security testing: `@WithMockUser` and seeded users (teacher/student) for role checks
- DB flush: `TestEntityManager.flush()` to force SQL/constraint checks

---

## How to run single tests (examples)
- Run one test class: `./mvnw -Dtest=StudentServiceTest test`
- Run a single unit test method: `./mvnw -Dtest=StudentServiceTest#testFindAll_Success test`
- Run integration tests (uses `application-test.yml` / H2 profile): `./mvnw -Dtest=CourseControllerIntegrationTest test`

---

If you want this summary added into `README.md` or a different file, indicate where and I will add it. ✅
