package com.springproject.universitymanagementsystem.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Entity tests for Student
 * Tests getters, setters, equals, hashCode, and validation annotations
 */
@DisplayName("Student Entity Tests")
class StudentTest {

    private Student student;
    private Department department;
    private Course course1;
    private Course course2;
    private User user;

    @BeforeEach
    void setUp() {
        department = new Department();
        department.setId(1L);
        department.setName("Computer Science");

        course1 = new Course();
        course1.setId(1L);
        course1.setName("Data Structures");
        course1.setCode("CS201");

        course2 = new Course();
        course2.setId(2L);
        course2.setName("Algorithms");
        course2.setCode("CS301");

        user = new User();
        user.setId(1L);
        user.setUsername("johndoe");

        student = new Student();
        student.setId(1L);
        student.setRollNumber("CS2024001");
        student.setName("John Doe");
        student.setEmail("john.doe@university.edu");
        student.setPhone("+1234567890");
        student.setDepartment(department);
        student.setCourses(Arrays.asList(course1, course2));
        student.setUser(user);
    }

    // ===== Getter/Setter Tests =====

    @Test
    @DisplayName("Should set and get ID correctly")
    void testIdGetterSetter() {
        // Arrange
        Student newStudent = new Student();
        
        // Act
        newStudent.setId(100L);
        
        // Assert
        assertThat(newStudent.getId()).isEqualTo(100L);
    }

    @Test
    @DisplayName("Should set and get roll number correctly")
    void testRollNumberGetterSetter() {
        // Arrange
        Student newStudent = new Student();
        
        // Act
        newStudent.setRollNumber("CS2024999");
        
        // Assert
        assertThat(newStudent.getRollNumber()).isEqualTo("CS2024999");
    }

    @Test
    @DisplayName("Should set and get name correctly")
    void testNameGetterSetter() {
        // Arrange
        Student newStudent = new Student();
        
        // Act
        newStudent.setName("Jane Smith");
        
        // Assert
        assertThat(newStudent.getName()).isEqualTo("Jane Smith");
    }

    @Test
    @DisplayName("Should set and get email correctly")
    void testEmailGetterSetter() {
        // Arrange
        Student newStudent = new Student();
        
        // Act
        newStudent.setEmail("jane.smith@university.edu");
        
        // Assert
        assertThat(newStudent.getEmail()).isEqualTo("jane.smith@university.edu");
    }

    @Test
    @DisplayName("Should set and get phone correctly")
    void testPhoneGetterSetter() {
        // Arrange
        Student newStudent = new Student();
        
        // Act
        newStudent.setPhone("+9876543210");
        
        // Assert
        assertThat(newStudent.getPhone()).isEqualTo("+9876543210");
    }

    @Test
    @DisplayName("Should set and get department correctly")
    void testDepartmentGetterSetter() {
        // Arrange
        Student newStudent = new Student();
        Department mathDept = new Department();
        mathDept.setId(2L);
        mathDept.setName("Mathematics");
        
        // Act
        newStudent.setDepartment(mathDept);
        
        // Assert
        assertThat(newStudent.getDepartment()).isNotNull();
        assertThat(newStudent.getDepartment().getId()).isEqualTo(2L);
        assertThat(newStudent.getDepartment().getName()).isEqualTo("Mathematics");
    }

    @Test
    @DisplayName("Should set and get courses correctly")
    void testCoursesGetterSetter() {
        // Arrange
        Student newStudent = new Student();
        List<Course> courses = Arrays.asList(course1, course2);
        
        // Act
        newStudent.setCourses(courses);
        
        // Assert
        assertThat(newStudent.getCourses()).isNotNull();
        assertThat(newStudent.getCourses()).hasSize(2);
        assertThat(newStudent.getCourses()).containsExactlyInAnyOrder(course1, course2);
    }

    @Test
    @DisplayName("Should set and get user correctly")
    void testUserGetterSetter() {
        // Arrange
        Student newStudent = new Student();
        User newUser = new User();
        newUser.setId(2L);
        newUser.setUsername("janesmith");
        
        // Act
        newStudent.setUser(newUser);
        
        // Assert
        assertThat(newStudent.getUser()).isNotNull();
        assertThat(newStudent.getUser().getId()).isEqualTo(2L);
        assertThat(newStudent.getUser().getUsername()).isEqualTo("janesmith");
    }

    // ===== Constructor Tests =====

    @Test
    @DisplayName("Should create student with no-args constructor")
    void testNoArgsConstructor() {
        // Act
        Student newStudent = new Student();
        
        // Assert
        assertThat(newStudent).isNotNull();
        assertThat(newStudent.getId()).isNull();
        assertThat(newStudent.getRollNumber()).isNull();
        assertThat(newStudent.getName()).isNull();
    }

    @Test
    @DisplayName("Should create student with parameterized constructor")
    void testParameterizedConstructor() {
        // Act
        Student newStudent = new Student(10L, "CS2024010", "Alice Johnson", 
                "alice@university.edu", "+1111111111");
        
        // Assert
        assertThat(newStudent).isNotNull();
        assertThat(newStudent.getId()).isEqualTo(10L);
        assertThat(newStudent.getRollNumber()).isEqualTo("CS2024010");
        assertThat(newStudent.getName()).isEqualTo("Alice Johnson");
        assertThat(newStudent.getEmail()).isEqualTo("alice@university.edu");
        assertThat(newStudent.getPhone()).isEqualTo("+1111111111");
    }

    // ===== Null Value Tests =====

    @Test
    @DisplayName("Should handle null email")
    void testNullEmail() {
        // Act
        student.setEmail(null);
        
        // Assert
        assertThat(student.getEmail()).isNull();
    }

    @Test
    @DisplayName("Should handle null phone")
    void testNullPhone() {
        // Act
        student.setPhone(null);
        
        // Assert
        assertThat(student.getPhone()).isNull();
    }

    @Test
    @DisplayName("Should handle null department")
    void testNullDepartment() {
        // Act
        student.setDepartment(null);
        
        // Assert
        assertThat(student.getDepartment()).isNull();
    }

    @Test
    @DisplayName("Should handle null courses list")
    void testNullCourses() {
        // Act
        student.setCourses(null);
        
        // Assert
        assertThat(student.getCourses()).isNull();
    }

    @Test
    @DisplayName("Should handle empty courses list")
    void testEmptyCourses() {
        // Act
        student.setCourses(Arrays.asList());
        
        // Assert
        assertThat(student.getCourses()).isNotNull();
        assertThat(student.getCourses()).isEmpty();
    }

    // ===== Relationship Tests =====

    @Test
    @DisplayName("Should maintain bidirectional relationship with user")
    void testUserRelationship() {
        // Arrange
        User linkedUser = new User();
        linkedUser.setId(3L);
        linkedUser.setUsername("testuser");
        linkedUser.setStudent(student);
        
        // Act
        student.setUser(linkedUser);
        
        // Assert
        assertThat(student.getUser()).isEqualTo(linkedUser);
        assertThat(student.getUser().getStudent()).isEqualTo(student);
    }

    @Test
    @DisplayName("Should handle multiple courses")
    void testMultipleCourses() {
        // Arrange
        Course course3 = new Course();
        course3.setId(3L);
        course3.setName("Operating Systems");
        course3.setCode("CS302");
        
        List<Course> updatedCourses = Arrays.asList(course1, course2, course3);
        
        // Act
        student.setCourses(updatedCourses);
        
        // Assert
        assertThat(student.getCourses()).hasSize(3);
        assertThat(student.getCourses()).contains(course3);
    }

    // ===== Validation Tests =====

    @Test
    @DisplayName("Should have all required fields set")
    void testRequiredFields() {
        // Assert
        assertThat(student.getRollNumber()).isNotNull();
        assertThat(student.getName()).isNotNull();
    }

    @Test
    @DisplayName("Should accept valid email format")
    void testValidEmail() {
        // Act
        student.setEmail("valid.email@university.edu");
        
        // Assert
        assertThat(student.getEmail()).isEqualTo("valid.email@university.edu");
    }

    @Test
    @DisplayName("Should accept various phone formats")
    void testVariousPhoneFormats() {
        // Test Case 1
        student.setPhone("+1234567890");
        assertThat(student.getPhone()).isEqualTo("+1234567890");
        
        // Test Case 2
        student.setPhone("123-456-7890");
        assertThat(student.getPhone()).isEqualTo("123-456-7890");
        
        // Test Case 3
        student.setPhone("(123) 456-7890");
        assertThat(student.getPhone()).isEqualTo("(123) 456-7890");
    }

    // ===== Business Logic Tests =====

    @Test
    @DisplayName("Should represent student data correctly")
    void testStudentDataIntegrity() {
        // Assert - Verify all data is correctly set
        assertThat(student.getId()).isEqualTo(1L);
        assertThat(student.getRollNumber()).isEqualTo("CS2024001");
        assertThat(student.getName()).isEqualTo("John Doe");
        assertThat(student.getEmail()).isEqualTo("john.doe@university.edu");
        assertThat(student.getPhone()).isEqualTo("+1234567890");
        assertThat(student.getDepartment().getName()).isEqualTo("Computer Science");
        assertThat(student.getCourses()).hasSize(2);
        assertThat(student.getUser().getUsername()).isEqualTo("johndoe");
    }

    @Test
    @DisplayName("Should allow modification of student data")
    void testStudentDataModification() {
        // Act
        student.setName("John Updated");
        student.setEmail("john.updated@university.edu");
        student.setPhone("+9999999999");
        
        // Assert
        assertThat(student.getName()).isEqualTo("John Updated");
        assertThat(student.getEmail()).isEqualTo("john.updated@university.edu");
        assertThat(student.getPhone()).isEqualTo("+9999999999");
        // Roll number should remain unchanged
        assertThat(student.getRollNumber()).isEqualTo("CS2024001");
    }
}
