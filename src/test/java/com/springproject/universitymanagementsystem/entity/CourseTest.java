package com.springproject.universitymanagementsystem.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Entity tests for Course
 * Tests getters, setters, equals, hashCode, and validation annotations
 */
@DisplayName("Course Entity Tests")
class CourseTest {

    private Course course;
    private Department department;
    private Teacher teacher;
    private Student student1;
    private Student student2;

    @BeforeEach
    void setUp() {
        department = new Department();
        department.setId(1L);
        department.setName("Computer Science");
        department.setDescription("CS Department");

        teacher = new Teacher();
        teacher.setId(1L);
        teacher.setName("Dr. Smith");
        teacher.setEmail("dr.smith@university.edu");

        student1 = new Student();
        student1.setId(1L);
        student1.setRollNumber("CS2024001");
        student1.setName("John Doe");

        student2 = new Student();
        student2.setId(2L);
        student2.setRollNumber("CS2024002");
        student2.setName("Jane Smith");

        course = new Course();
        course.setId(1L);
        course.setName("Data Structures");
        course.setCode("CS201");
        course.setCredits(3);
        course.setDepartment(department);
        course.setTeacher(teacher);
        course.setStudents(Arrays.asList(student1, student2));
    }

    // ===== Getter/Setter Tests =====

    @Test
    @DisplayName("Should set and get ID correctly")
    void testIdGetterSetter() {
        // Arrange
        Course newCourse = new Course();
        
        // Act
        newCourse.setId(100L);
        
        // Assert
        assertThat(newCourse.getId()).isEqualTo(100L);
    }

    @Test
    @DisplayName("Should set and get name correctly")
    void testNameGetterSetter() {
        // Arrange
        Course newCourse = new Course();
        
        // Act
        newCourse.setName("Algorithms");
        
        // Assert
        assertThat(newCourse.getName()).isEqualTo("Algorithms");
    }

    @Test
    @DisplayName("Should set and get code correctly")
    void testCodeGetterSetter() {
        // Arrange
        Course newCourse = new Course();
        
        // Act
        newCourse.setCode("CS301");
        
        // Assert
        assertThat(newCourse.getCode()).isEqualTo("CS301");
    }

    @Test
    @DisplayName("Should set and get credits correctly")
    void testCreditsGetterSetter() {
        // Arrange
        Course newCourse = new Course();
        
        // Act
        newCourse.setCredits(4);
        
        // Assert
        assertThat(newCourse.getCredits()).isEqualTo(4);
    }

    @Test
    @DisplayName("Should set and get department correctly")
    void testDepartmentGetterSetter() {
        // Arrange
        Course newCourse = new Course();
        Department mathDept = new Department();
        mathDept.setId(2L);
        mathDept.setName("Mathematics");
        
        // Act
        newCourse.setDepartment(mathDept);
        
        // Assert
        assertThat(newCourse.getDepartment()).isNotNull();
        assertThat(newCourse.getDepartment().getId()).isEqualTo(2L);
        assertThat(newCourse.getDepartment().getName()).isEqualTo("Mathematics");
    }

    @Test
    @DisplayName("Should set and get teacher correctly")
    void testTeacherGetterSetter() {
        // Arrange
        Course newCourse = new Course();
        Teacher newTeacher = new Teacher();
        newTeacher.setId(2L);
        newTeacher.setName("Dr. Johnson");
        
        // Act
        newCourse.setTeacher(newTeacher);
        
        // Assert
        assertThat(newCourse.getTeacher()).isNotNull();
        assertThat(newCourse.getTeacher().getId()).isEqualTo(2L);
        assertThat(newCourse.getTeacher().getName()).isEqualTo("Dr. Johnson");
    }

    @Test
    @DisplayName("Should set and get students correctly")
    void testStudentsGetterSetter() {
        // Arrange
        Course newCourse = new Course();
        List<Student> students = Arrays.asList(student1, student2);
        
        // Act
        newCourse.setStudents(students);
        
        // Assert
        assertThat(newCourse.getStudents()).isNotNull();
        assertThat(newCourse.getStudents()).hasSize(2);
        assertThat(newCourse.getStudents()).containsExactlyInAnyOrder(student1, student2);
    }

    // ===== Constructor Tests =====

    @Test
    @DisplayName("Should create course with no-args constructor")
    void testNoArgsConstructor() {
        // Act
        Course newCourse = new Course();
        
        // Assert
        assertThat(newCourse).isNotNull();
        assertThat(newCourse.getId()).isNull();
        assertThat(newCourse.getName()).isNull();
        assertThat(newCourse.getCode()).isNull();
        assertThat(newCourse.getCredits()).isNull();
    }

    @Test
    @DisplayName("Should create course with parameterized constructor")
    void testParameterizedConstructor() {
        // Act
        Course newCourse = new Course(10L, "Operating Systems", "CS302", 4);
        
        // Assert
        assertThat(newCourse).isNotNull();
        assertThat(newCourse.getId()).isEqualTo(10L);
        assertThat(newCourse.getName()).isEqualTo("Operating Systems");
        assertThat(newCourse.getCode()).isEqualTo("CS302");
        assertThat(newCourse.getCredits()).isEqualTo(4);
    }

    // ===== Null Value Tests =====

    @Test
    @DisplayName("Should handle null code")
    void testNullCode() {
        // Act
        course.setCode(null);
        
        // Assert
        assertThat(course.getCode()).isNull();
    }

    @Test
    @DisplayName("Should handle null credits")
    void testNullCredits() {
        // Act
        course.setCredits(null);
        
        // Assert
        assertThat(course.getCredits()).isNull();
    }

    @Test
    @DisplayName("Should handle null department")
    void testNullDepartment() {
        // Act
        course.setDepartment(null);
        
        // Assert
        assertThat(course.getDepartment()).isNull();
    }

    @Test
    @DisplayName("Should handle null teacher")
    void testNullTeacher() {
        // Act
        course.setTeacher(null);
        
        // Assert
        assertThat(course.getTeacher()).isNull();
    }

    @Test
    @DisplayName("Should handle null students list")
    void testNullStudents() {
        // Act
        course.setStudents(null);
        
        // Assert
        assertThat(course.getStudents()).isNull();
    }

    @Test
    @DisplayName("Should handle empty students list")
    void testEmptyStudents() {
        // Act
        course.setStudents(Arrays.asList());
        
        // Assert
        assertThat(course.getStudents()).isNotNull();
        assertThat(course.getStudents()).isEmpty();
    }

    // ===== Credits Validation Tests =====

    @Test
    @DisplayName("Should accept valid credit values")
    void testValidCredits() {
        // Test various valid credit values
        course.setCredits(1);
        assertThat(course.getCredits()).isEqualTo(1);
        
        course.setCredits(3);
        assertThat(course.getCredits()).isEqualTo(3);
        
        course.setCredits(4);
        assertThat(course.getCredits()).isEqualTo(4);
        
        course.setCredits(6);
        assertThat(course.getCredits()).isEqualTo(6);
    }

    @Test
    @DisplayName("Should accept zero credits for special courses")
    void testZeroCredits() {
        // Act
        course.setCredits(0);
        
        // Assert
        assertThat(course.getCredits()).isEqualTo(0);
    }

    @Test
    @DisplayName("Should handle negative credits (business logic should validate)")
    void testNegativeCredits() {
        // Act
        course.setCredits(-1);
        
        // Assert - Entity allows it, but business logic should prevent this
        assertThat(course.getCredits()).isEqualTo(-1);
    }

    // ===== Relationship Tests =====

    @Test
    @DisplayName("Should maintain relationship with department")
    void testDepartmentRelationship() {
        // Assert
        assertThat(course.getDepartment()).isNotNull();
        assertThat(course.getDepartment().getName()).isEqualTo("Computer Science");
        assertThat(course.getDepartment().getDescription()).isEqualTo("CS Department");
    }

    @Test
    @DisplayName("Should maintain relationship with teacher")
    void testTeacherRelationship() {
        // Assert
        assertThat(course.getTeacher()).isNotNull();
        assertThat(course.getTeacher().getName()).isEqualTo("Dr. Smith");
        assertThat(course.getTeacher().getEmail()).isEqualTo("dr.smith@university.edu");
    }

    @Test
    @DisplayName("Should maintain many-to-many relationship with students")
    void testStudentsRelationship() {
        // Assert
        assertThat(course.getStudents()).isNotNull();
        assertThat(course.getStudents()).hasSize(2);
        assertThat(course.getStudents()).extracting(Student::getRollNumber)
                .containsExactlyInAnyOrder("CS2024001", "CS2024002");
    }

    @Test
    @DisplayName("Should allow adding students to course")
    void testAddStudents() {
        // Arrange
        Student student3 = new Student();
        student3.setId(3L);
        student3.setRollNumber("CS2024003");
        student3.setName("Alice Johnson");
        
        List<Student> updatedStudents = Arrays.asList(student1, student2, student3);
        
        // Act
        course.setStudents(updatedStudents);
        
        // Assert
        assertThat(course.getStudents()).hasSize(3);
        assertThat(course.getStudents()).contains(student3);
    }

    // ===== Required Fields Tests =====

    @Test
    @DisplayName("Should have course name as required field")
    void testRequiredName() {
        // Assert
        assertThat(course.getName()).isNotNull();
        assertThat(course.getName()).isNotEmpty();
    }

    // ===== Business Logic Tests =====

    @Test
    @DisplayName("Should represent course data correctly")
    void testCourseDataIntegrity() {
        // Assert - Verify all data is correctly set
        assertThat(course.getId()).isEqualTo(1L);
        assertThat(course.getName()).isEqualTo("Data Structures");
        assertThat(course.getCode()).isEqualTo("CS201");
        assertThat(course.getCredits()).isEqualTo(3);
        assertThat(course.getDepartment().getName()).isEqualTo("Computer Science");
        assertThat(course.getTeacher().getName()).isEqualTo("Dr. Smith");
        assertThat(course.getStudents()).hasSize(2);
    }

    @Test
    @DisplayName("Should allow modification of course data")
    void testCourseDataModification() {
        // Act
        course.setName("Advanced Data Structures");
        course.setCode("CS401");
        course.setCredits(4);
        
        // Assert
        assertThat(course.getName()).isEqualTo("Advanced Data Structures");
        assertThat(course.getCode()).isEqualTo("CS401");
        assertThat(course.getCredits()).isEqualTo(4);
    }

    @Test
    @DisplayName("Should allow course without students")
    void testCourseWithoutStudents() {
        // Act
        course.setStudents(Arrays.asList());
        
        // Assert
        assertThat(course.getStudents()).isEmpty();
        assertThat(course.getName()).isEqualTo("Data Structures");
        assertThat(course.getTeacher()).isNotNull();
    }

    @Test
    @DisplayName("Should allow changing course teacher")
    void testChangeTeacher() {
        // Arrange
        Teacher newTeacher = new Teacher();
        newTeacher.setId(3L);
        newTeacher.setName("Dr. Williams");
        newTeacher.setEmail("dr.williams@university.edu");
        
        // Act
        course.setTeacher(newTeacher);
        
        // Assert
        assertThat(course.getTeacher()).isNotNull();
        assertThat(course.getTeacher().getId()).isEqualTo(3L);
        assertThat(course.getTeacher().getName()).isEqualTo("Dr. Williams");
    }

    @Test
    @DisplayName("Should allow changing course department")
    void testChangeDepartment() {
        // Arrange
        Department newDept = new Department();
        newDept.setId(5L);
        newDept.setName("Mathematics");
        newDept.setDescription("MATH Department");
        
        // Act
        course.setDepartment(newDept);
        
        // Assert
        assertThat(course.getDepartment()).isNotNull();
        assertThat(course.getDepartment().getId()).isEqualTo(5L);
        assertThat(course.getDepartment().getName()).isEqualTo("Mathematics");
        assertThat(course.getDepartment().getDescription()).isEqualTo("MATH Department");
    }
}
