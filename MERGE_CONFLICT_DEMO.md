# 🔥 Merge Conflict Resolution - Practical Demo

## 📌 Overview
This document provides hands-on examples of merge conflicts and their resolutions.

---

## 🎯 Scenario 1: Simple Conflict in Service Layer

### Setup
Two developers working on the same file simultaneously:

**Developer A** (on `master`):
```java
// StudentService.java - Line 15-20
public Student save(Student student) {
    // Validate student before saving
    if (student.getRollNumber() == null) {
        throw new IllegalArgumentException("Roll number cannot be null");
    }
    return studentRepository.save(student);
}
```

**Developer B** (on `testing/unit-integration-tests`):
```java
// StudentService.java - Line 15-20
public Student save(Student student) {
    // Log student creation
    logger.info("Saving student: {}", student.getRollNumber());
    return studentRepository.save(student);
}
```

### Conflict After Merge

```java
public Student save(Student student) {
<<<<<<< HEAD (testing/unit-integration-tests)
    // Log student creation
    logger.info("Saving student: {}", student.getRollNumber());
=======
    // Validate student before saving
    if (student.getRollNumber() == null) {
        throw new IllegalArgumentException("Roll number cannot be null");
    }
>>>>>>> master
    return studentRepository.save(student);
}
```

### Resolution (Best Practice - Combine Both)

```java
public Student save(Student student) {
    // Validate student before saving
    if (student.getRollNumber() == null) {
        throw new IllegalArgumentException("Roll number cannot be null");
    }
    // Log student creation
    logger.info("Saving student: {}", student.getRollNumber());
    return studentRepository.save(student);
}
```

### Commands
```bash
# On testing branch
git fetch origin
git merge origin/master

# Conflict appears - edit file
# After resolving:
git add src/main/java/.../StudentService.java
git commit -m "merge: resolve conflict in StudentService - combine validation and logging"
git push origin testing/unit-integration-tests
```

---

## 🎯 Scenario 2: Configuration File Conflict

### Setup

**Master branch** (`application.properties`):
```properties
# Database Configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/university_db
spring.datasource.username=admin
spring.datasource.password=admin123

# JPA Configuration
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=false
```

**Testing branch** (`application.properties`):
```properties
# Database Configuration
spring.datasource.url=jdbc:postgresql://localhost:5432/university_db
spring.datasource.username=admin
spring.datasource.password=admin123

# JPA Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

### Conflict

```properties
# JPA Configuration
<<<<<<< HEAD (testing branch)
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
=======
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=false
>>>>>>> master
```

### Resolution (Choose Production Settings)

```properties
# JPA Configuration - Production ready
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=false

# For testing, use application-test.yml instead
```

**Explanation**: 
- Keep `validate` for production safety
- Keep `show-sql=false` for performance
- Testing configuration goes in `application-test.yml`

---

## 🎯 Scenario 3: Test File Conflict

### Setup

**Master branch** (new test added):
```java
@Test
void testFindAll_Success() {
    List<Student> students = Arrays.asList(student1, student2);
    when(studentRepository.findAll()).thenReturn(students);
    
    List<Student> result = studentService.findAll();
    
    assertThat(result).hasSize(2);
}
```

**Testing branch** (enhanced test added):
```java
@Test
void testFindAll_Success() {
    List<Student> students = Arrays.asList(student1, student2, student3);
    when(studentRepository.findAll()).thenReturn(students);
    
    List<Student> result = studentService.findAll();
    
    assertThat(result).hasSize(3);
    assertThat(result).containsExactlyInAnyOrder(student1, student2, student3);
}
```

### Conflict

```java
@Test
void testFindAll_Success() {
<<<<<<< HEAD (testing branch)
    List<Student> students = Arrays.asList(student1, student2, student3);
    when(studentRepository.findAll()).thenReturn(students);
    
    List<Student> result = studentService.findAll();
    
    assertThat(result).hasSize(3);
    assertThat(result).containsExactlyInAnyOrder(student1, student2, student3);
=======
    List<Student> students = Arrays.asList(student1, student2);
    when(studentRepository.findAll()).thenReturn(students);
    
    List<Student> result = studentService.findAll();
    
    assertThat(result).hasSize(2);
>>>>>>> master
}
```

### Resolution (Keep More Comprehensive Test)

```java
@Test
@DisplayName("Should return all students successfully")
void testFindAll_Success() {
    // Arrange - Use comprehensive test data
    List<Student> students = Arrays.asList(student1, student2, student3);
    when(studentRepository.findAll()).thenReturn(students);
    
    // Act
    List<Student> result = studentService.findAll();
    
    // Assert - More comprehensive assertions
    assertThat(result).isNotNull();
    assertThat(result).hasSize(3);
    assertThat(result).containsExactlyInAnyOrder(student1, student2, student3);
    verify(studentRepository, times(1)).findAll();
}
```

---

## 🎯 Scenario 4: Dependency Conflict (pom.xml)

### Setup

**Master branch**:
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <scope>runtime</scope>
</dependency>
```

**Testing branch**:
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>org.mockito</groupId>
    <artifactId>mockito-core</artifactId>
    <scope>test</scope>
</dependency>
```

### Conflict

```xml
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
<<<<<<< HEAD (testing branch)
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>org.mockito</groupId>
    <artifactId>mockito-core</artifactId>
    <scope>test</scope>
=======
    <scope>runtime</scope>
>>>>>>> master
</dependency>
```

### Resolution (Merge Dependencies)

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <scope>test</scope>
    <!-- Use test scope for H2 - only for testing -->
</dependency>
<!-- mockito-core is included in spring-boot-starter-test, no need to add separately -->
```

---

## 🎯 Scenario 5: Complex Multi-File Conflict

### Files in Conflict
1. `StudentService.java`
2. `StudentController.java`
3. `StudentServiceTest.java`

### Resolution Strategy

```bash
# Step 1: Fetch latest
git fetch origin

# Step 2: Check what will be merged
git log HEAD..origin/master --oneline

# Step 3: Attempt merge
git merge origin/master

# Output shows conflicts:
# CONFLICT (content): Merge conflict in StudentService.java
# CONFLICT (content): Merge conflict in StudentController.java
# CONFLICT (content): Merge conflict in StudentServiceTest.java

# Step 4: Check conflict status
git status

# Step 5: Resolve files one by one
# Open StudentService.java - resolve
git add src/main/java/.../StudentService.java

# Open StudentController.java - resolve
git add src/main/java/.../StudentController.java

# Open StudentServiceTest.java - resolve
git add src/test/java/.../StudentServiceTest.java

# Step 6: Verify no more conflicts
git status

# Step 7: Complete merge
git commit -m "merge: resolve conflicts in Student components

- Combined validation and logging in StudentService
- Preserved new endpoint in StudentController
- Kept comprehensive test coverage in StudentServiceTest"

# Step 8: Run tests to ensure nothing broke
mvn clean test

# Step 9: Push if tests pass
git push origin testing/unit-integration-tests
```

---

## 🛠️ Tools for Conflict Resolution

### 1. VSCode Built-in Merge Tool

VSCode shows conflicts with buttons:
- **Accept Current Change** - Keep your changes
- **Accept Incoming Change** - Keep master changes
- **Accept Both Changes** - Keep both
- **Compare Changes** - View side-by-side

### 2. Git Command Line

```bash
# View conflict
git diff

# Show conflicts in detail
git diff --name-only --diff-filter=U

# Abort merge if needed
git merge --abort

# Abort rebase if needed
git rebase --abort
```

### 3. IntelliJ IDEA / Android Studio

- Right-click file → Git → Resolve Conflicts
- Use 3-way merge tool
- Shows: Left (yours), Center (base), Right (theirs)

---

## ⚠️ Common Mistakes

### ❌ Mistake 1: Accepting All Without Reading
```bash
# DON'T do this blindly:
git checkout --ours .
git checkout --theirs .
```

### ❌ Mistake 2: Deleting Conflict Markers Without Understanding
```java
// DON'T just delete these without reading:
<<<<<<< HEAD
=======
>>>>>>> master
```

### ❌ Mistake 3: Force Pushing to Master
```bash
# NEVER do this:
git push origin master --force
```

### ❌ Mistake 4: Not Testing After Resolution
```bash
# ALWAYS test after resolving:
mvn clean test
# If tests pass, then push
```

---

## ✅ Best Practices

### 1. Prevent Conflicts
```bash
# Pull frequently
git pull origin master

# Keep branches short-lived
# Aim to merge within 1-3 days

# Communicate with team
# Use project management tools
```

### 2. Resolve Early
```bash
# Don't let conflicts accumulate
# Resolve as soon as you see them

# Sync with master daily
git fetch origin
git merge origin/master
```

### 3. Understand Before Resolving
- Read both versions carefully
- Understand the intent
- Ask the other developer if unclear
- Test thoroughly after resolution

### 4. Use Proper Tools
- Use IDE merge tools
- Visual diff tools help
- Don't rely only on command line

---

## 📊 Conflict Resolution Checklist

Before resolving:
- [ ] Understand what both changes do
- [ ] Know which is more recent
- [ ] Understand business requirements
- [ ] Have backup of current state

During resolution:
- [ ] Read all conflict markers
- [ ] Keep necessary changes from both
- [ ] Remove all conflict markers
- [ ] Maintain code style
- [ ] Preserve comments and documentation

After resolution:
- [ ] Run `mvn clean test`
- [ ] Manual testing of affected features
- [ ] Code review the resolution
- [ ] Document complex resolutions
- [ ] Push with descriptive commit message

---

## 🎓 Learning Resources

### Practice Conflicts
```bash
# Create a practice repository
git init conflict-practice
cd conflict-practice

# Create base file
echo "Line 1" > test.txt
git add test.txt
git commit -m "Initial commit"

# Create branch and modify
git checkout -b feature
echo "Line 2 from feature" >> test.txt
git commit -am "Feature change"

# Go back to master and modify same line
git checkout master
echo "Line 2 from master" >> test.txt
git commit -am "Master change"

# Try to merge - conflict!
git merge feature

# Practice resolving
```

---

## 📝 Summary

**Key Takeaways**:
1. ✅ Conflicts are normal in team development
2. ✅ Read and understand both changes
3. ✅ Combine changes when possible
4. ✅ Test thoroughly after resolution
5. ✅ Communicate with team
6. ✅ Use proper tools
7. ✅ Document complex resolutions

**Remember**:
> "The best merge conflict is the one you prevent through communication and frequent syncing"

---

**Last Updated**: February 16, 2026  
**Version**: 1.0.0
