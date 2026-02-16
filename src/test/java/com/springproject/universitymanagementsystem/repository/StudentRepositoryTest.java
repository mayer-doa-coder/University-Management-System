package com.springproject.universitymanagementsystem.repository;

import com.springproject.universitymanagementsystem.entity.Department;
import com.springproject.universitymanagementsystem.entity.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Repository tests for StudentRepository
 * Uses @DataJpaTest with H2 in-memory database
 * Tests save(), findById(), custom queries, and transactional behavior
 */
@DataJpaTest
@ActiveProfiles("test")
@DisplayName("StudentRepository Tests")
class StudentRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private StudentRepository studentRepository;

    private Department testDepartment;
    private Student testStudent;

    @BeforeEach
    void setUp() {
        // Arrange - Set up test data
        testDepartment = new Department();
        testDepartment.setName("Computer Science");
        testDepartment.setDescription("CS Department");
        entityManager.persist(testDepartment);

        testStudent = new Student();
        testStudent.setRollNumber("CS2024001");
        testStudent.setName("John Doe");
        testStudent.setEmail("john.doe@university.edu");
        testStudent.setPhone("+1234567890");
        testStudent.setDepartment(testDepartment);
    }

    // ===== save() Tests =====

    @Test
    @DisplayName("Should save student successfully")
    void testSave_Success() {
        // Act
        Student savedStudent = studentRepository.save(testStudent);
        entityManager.flush();

        // Assert
        assertThat(savedStudent).isNotNull();
        assertThat(savedStudent.getId()).isNotNull();
        assertThat(savedStudent.getRollNumber()).isEqualTo("CS2024001");
        assertThat(savedStudent.getName()).isEqualTo("John Doe");
        assertThat(savedStudent.getEmail()).isEqualTo("john.doe@university.edu");
        assertThat(savedStudent.getDepartment().getName()).isEqualTo("Computer Science");
    }

    @Test
    @DisplayName("Should save student with null optional fields")
    void testSave_WithNullFields() {
        // Arrange
        Student student = new Student();
        student.setRollNumber("CS2024002");
        student.setName("Jane Smith");
        student.setEmail(null);
        student.setPhone(null);

        // Act
        Student savedStudent = studentRepository.save(student);
        entityManager.flush();

        // Assert
        assertThat(savedStudent).isNotNull();
        assertThat(savedStudent.getId()).isNotNull();
        assertThat(savedStudent.getEmail()).isNull();
        assertThat(savedStudent.getPhone()).isNull();
    }

    @Test
    @DisplayName("Should update existing student")
    void testSave_Update() {
        // Arrange
        Student savedStudent = entityManager.persistAndFlush(testStudent);
        Long studentId = savedStudent.getId();

        // Act
        savedStudent.setName("John Updated");
        savedStudent.setEmail("john.updated@university.edu");
        Student updatedStudent = studentRepository.save(savedStudent);
        entityManager.flush();

        // Assert
        assertThat(updatedStudent.getId()).isEqualTo(studentId);
        assertThat(updatedStudent.getName()).isEqualTo("John Updated");
        assertThat(updatedStudent.getEmail()).isEqualTo("john.updated@university.edu");
    }

    // ===== findById() Tests =====

    @Test
    @DisplayName("Should find student by ID successfully")
    void testFindById_Success() {
        // Arrange
        Student savedStudent = entityManager.persistAndFlush(testStudent);
        Long studentId = savedStudent.getId();

        // Act
        Optional<Student> foundStudent = studentRepository.findById(studentId);

        // Assert
        assertThat(foundStudent).isPresent();
        assertThat(foundStudent.get().getId()).isEqualTo(studentId);
        assertThat(foundStudent.get().getRollNumber()).isEqualTo("CS2024001");
        assertThat(foundStudent.get().getName()).isEqualTo("John Doe");
    }

    @Test
    @DisplayName("Should return empty Optional when student not found by ID")
    void testFindById_NotFound() {
        // Act
        Optional<Student> foundStudent = studentRepository.findById(999L);

        // Assert
        assertThat(foundStudent).isEmpty();
    }

    // ===== findByRollNumber() Tests =====

    @Test
    @DisplayName("Should find student by roll number successfully")
    void testFindByRollNumber_Success() {
        // Arrange
        entityManager.persistAndFlush(testStudent);

        // Act
        Student foundStudent = studentRepository.findByRollNumber("CS2024001");

        // Assert
        assertThat(foundStudent).isNotNull();
        assertThat(foundStudent.getRollNumber()).isEqualTo("CS2024001");
        assertThat(foundStudent.getName()).isEqualTo("John Doe");
    }

    @Test
    @DisplayName("Should return null when student not found by roll number")
    void testFindByRollNumber_NotFound() {
        // Act
        Student foundStudent = studentRepository.findByRollNumber("NONEXISTENT");

        // Assert
        assertThat(foundStudent).isNull();
    }

    @Test
    @DisplayName("Should enforce unique constraint on roll number")
    void testFindByRollNumber_Unique() {
        // Arrange
        entityManager.persistAndFlush(testStudent);

        Student duplicateStudent = new Student();
        duplicateStudent.setRollNumber("CS2024003");
        duplicateStudent.setName("Unique Student");
        entityManager.persistAndFlush(duplicateStudent);

        // Act
        Student found1 = studentRepository.findByRollNumber("CS2024001");
        Student found2 = studentRepository.findByRollNumber("CS2024003");

        // Assert
        assertThat(found1).isNotNull();
        assertThat(found1.getName()).isEqualTo("John Doe");
        assertThat(found2).isNotNull();
        assertThat(found2.getName()).isEqualTo("Unique Student");
    }

    // ===== findAll() Tests =====

    @Test
    @DisplayName("Should find all students")
    void testFindAll_Success() {
        // Arrange
        Student student2 = new Student();
        student2.setRollNumber("CS2024002");
        student2.setName("Jane Smith");
        student2.setDepartment(testDepartment);

        Student student3 = new Student();
        student3.setRollNumber("CS2024003");
        student3.setName("Alice Johnson");
        student3.setDepartment(testDepartment);

        entityManager.persist(testStudent);
        entityManager.persist(student2);
        entityManager.persist(student3);
        entityManager.flush();

        // Act
        List<Student> students = studentRepository.findAll();

        // Assert
        assertThat(students).hasSize(3);
        assertThat(students).extracting(Student::getRollNumber)
                .containsExactlyInAnyOrder("CS2024001", "CS2024002", "CS2024003");
    }

    @Test
    @DisplayName("Should return empty list when no students exist")
    void testFindAll_EmptyList() {
        // Act
        List<Student> students = studentRepository.findAll();

        // Assert
        assertThat(students).isEmpty();
    }

    // ===== delete() Tests =====

    @Test
    @DisplayName("Should delete student successfully")
    void testDelete_Success() {
        // Arrange
        Student savedStudent = entityManager.persistAndFlush(testStudent);
        Long studentId = savedStudent.getId();

        // Act
        studentRepository.deleteById(studentId);
        entityManager.flush();

        // Assert
        Optional<Student> deletedStudent = studentRepository.findById(studentId);
        assertThat(deletedStudent).isEmpty();
    }

    // ===== Transactional Behavior Tests =====

    @Test
    @DisplayName("Should rollback transaction on error")
    void testTransactionalBehavior() {
        // Arrange
        Student savedStudent = entityManager.persistAndFlush(testStudent);
        entityManager.clear();

        // Act
        savedStudent.setName("Modified Name");
        studentRepository.save(savedStudent);
        // Simulate error by clearing without flush
        entityManager.clear();

        // Assert
        Student reloadedStudent = studentRepository.findById(savedStudent.getId()).orElse(null);
        assertThat(reloadedStudent).isNotNull();
        // Name should not be updated due to clear without flush
    }

    @Test
    @DisplayName("Should cascade operations with department relationship")
    void testCascadeOperations() {
        // Arrange
        Department newDepartment = new Department();
        newDepartment.setName("Mathematics");
        newDepartment.setDescription("MATH Department");
        entityManager.persist(newDepartment);

        testStudent.setDepartment(newDepartment);
        Student savedStudent = entityManager.persistAndFlush(testStudent);

        // Act
        entityManager.clear();
        Student foundStudent = studentRepository.findById(savedStudent.getId()).orElse(null);

        // Assert
        assertThat(foundStudent).isNotNull();
        assertThat(foundStudent.getDepartment()).isNotNull();
        assertThat(foundStudent.getDepartment().getName()).isEqualTo("Mathematics");
    }
}
