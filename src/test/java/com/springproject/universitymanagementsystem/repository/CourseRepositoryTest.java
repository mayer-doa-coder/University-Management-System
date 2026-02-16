package com.springproject.universitymanagementsystem.repository;

import com.springproject.universitymanagementsystem.entity.Course;
import com.springproject.universitymanagementsystem.entity.Department;
import com.springproject.universitymanagementsystem.entity.Teacher;
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
 * Repository tests for CourseRepository
 * Uses @DataJpaTest with H2 in-memory database
 * Tests save(), findById(), custom queries, and transactional behavior
 */
@DataJpaTest
@ActiveProfiles("test")
@DisplayName("CourseRepository Tests")
class CourseRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CourseRepository courseRepository;

    private Department testDepartment;
    private Teacher testTeacher;
    private Course testCourse;

    @BeforeEach
    void setUp() {
        // Arrange - Set up test data
        testDepartment = new Department();
        testDepartment.setName("Computer Science");
        testDepartment.setDescription("CS Department");
        entityManager.persist(testDepartment);

        testTeacher = new Teacher();
        testTeacher.setName("Dr. Smith");
        testTeacher.setEmail("dr.smith@university.edu");
        testTeacher.setDepartment(testDepartment);
        entityManager.persist(testTeacher);

        testCourse = new Course();
        testCourse.setName("Data Structures");
        testCourse.setCode("CS201");
        testCourse.setCredits(3);
        testCourse.setDepartment(testDepartment);
        testCourse.setTeacher(testTeacher);
    }

    // ===== save() Tests =====

    @Test
    @DisplayName("Should save course successfully")
    void testSave_Success() {
        // Act
        Course savedCourse = courseRepository.save(testCourse);
        entityManager.flush();

        // Assert
        assertThat(savedCourse).isNotNull();
        assertThat(savedCourse.getId()).isNotNull();
        assertThat(savedCourse.getName()).isEqualTo("Data Structures");
        assertThat(savedCourse.getCode()).isEqualTo("CS201");
        assertThat(savedCourse.getCredits()).isEqualTo(3);
        assertThat(savedCourse.getDepartment().getName()).isEqualTo("Computer Science");
        assertThat(savedCourse.getTeacher().getName()).isEqualTo("Dr. Smith");
    }

    @Test
    @DisplayName("Should save course with null optional fields")
    void testSave_WithNullFields() {
        // Arrange
        Course course = new Course();
        course.setName("Special Topics");
        course.setCode(null);
        course.setCredits(null);

        // Act
        Course savedCourse = courseRepository.save(course);
        entityManager.flush();

        // Assert
        assertThat(savedCourse).isNotNull();
        assertThat(savedCourse.getId()).isNotNull();
        assertThat(savedCourse.getName()).isEqualTo("Special Topics");
        assertThat(savedCourse.getCode()).isNull();
        assertThat(savedCourse.getCredits()).isNull();
    }

    @Test
    @DisplayName("Should update existing course")
    void testSave_Update() {
        // Arrange
        Course savedCourse = entityManager.persistAndFlush(testCourse);
        Long courseId = savedCourse.getId();

        // Act
        savedCourse.setName("Advanced Data Structures");
        savedCourse.setCode("CS401");
        savedCourse.setCredits(4);
        Course updatedCourse = courseRepository.save(savedCourse);
        entityManager.flush();

        // Assert
        assertThat(updatedCourse.getId()).isEqualTo(courseId);
        assertThat(updatedCourse.getName()).isEqualTo("Advanced Data Structures");
        assertThat(updatedCourse.getCode()).isEqualTo("CS401");
        assertThat(updatedCourse.getCredits()).isEqualTo(4);
    }

    // ===== findById() Tests =====

    @Test
    @DisplayName("Should find course by ID successfully")
    void testFindById_Success() {
        // Arrange
        Course savedCourse = entityManager.persistAndFlush(testCourse);
        Long courseId = savedCourse.getId();

        // Act
        Optional<Course> foundCourse = courseRepository.findById(courseId);

        // Assert
        assertThat(foundCourse).isPresent();
        assertThat(foundCourse.get().getId()).isEqualTo(courseId);
        assertThat(foundCourse.get().getName()).isEqualTo("Data Structures");
        assertThat(foundCourse.get().getCode()).isEqualTo("CS201");
    }

    @Test
    @DisplayName("Should return empty Optional when course not found by ID")
    void testFindById_NotFound() {
        // Act
        Optional<Course> foundCourse = courseRepository.findById(999L);

        // Assert
        assertThat(foundCourse).isEmpty();
    }

    // ===== findAll() Tests =====

    @Test
    @DisplayName("Should find all courses")
    void testFindAll_Success() {
        // Arrange
        Course course2 = new Course();
        course2.setName("Algorithms");
        course2.setCode("CS301");
        course2.setCredits(4);
        course2.setDepartment(testDepartment);

        Course course3 = new Course();
        course3.setName("Database Systems");
        course3.setCode("CS202");
        course3.setCredits(3);
        course3.setDepartment(testDepartment);

        entityManager.persist(testCourse);
        entityManager.persist(course2);
        entityManager.persist(course3);
        entityManager.flush();

        // Act
        List<Course> courses = courseRepository.findAll();

        // Assert
        assertThat(courses).hasSize(3);
        assertThat(courses).extracting(Course::getCode)
                .containsExactlyInAnyOrder("CS201", "CS301", "CS202");
    }

    @Test
    @DisplayName("Should return empty list when no courses exist")
    void testFindAll_EmptyList() {
        // Act
        List<Course> courses = courseRepository.findAll();

        // Assert
        assertThat(courses).isEmpty();
    }

    // ===== delete() Tests =====

    @Test
    @DisplayName("Should delete course successfully")
    void testDelete_Success() {
        // Arrange
        Course savedCourse = entityManager.persistAndFlush(testCourse);
        Long courseId = savedCourse.getId();

        // Act
        courseRepository.deleteById(courseId);
        entityManager.flush();

        // Assert
        Optional<Course> deletedCourse = courseRepository.findById(courseId);
        assertThat(deletedCourse).isEmpty();
    }

    @Test
    @DisplayName("Should delete multiple courses")
    void testDelete_Multiple() {
        // Arrange
        Course course2 = new Course();
        course2.setName("Algorithms");
        course2.setCode("CS301");

        entityManager.persist(testCourse);
        entityManager.persist(course2);
        entityManager.flush();

        Long id1 = testCourse.getId();
        Long id2 = course2.getId();

        // Act
        courseRepository.deleteById(id1);
        entityManager.flush();

        // Assert
        assertThat(courseRepository.findById(id1)).isEmpty();
        assertThat(courseRepository.findById(id2)).isPresent();
    }

    // ===== Relationship Tests =====

    @Test
    @DisplayName("Should maintain relationship with department")
    void testDepartmentRelationship() {
        // Arrange
        Course savedCourse = entityManager.persistAndFlush(testCourse);
        entityManager.clear();

        // Act
        Course foundCourse = courseRepository.findById(savedCourse.getId()).orElse(null);

        // Assert
        assertThat(foundCourse).isNotNull();
        assertThat(foundCourse.getDepartment()).isNotNull();
        assertThat(foundCourse.getDepartment().getName()).isEqualTo("Computer Science");
        assertThat(foundCourse.getDepartment().getDescription()).isEqualTo("CS Department");
    }

    @Test
    @DisplayName("Should maintain relationship with teacher")
    void testTeacherRelationship() {
        // Arrange
        Course savedCourse = entityManager.persistAndFlush(testCourse);
        entityManager.clear();

        // Act
        Course foundCourse = courseRepository.findById(savedCourse.getId()).orElse(null);

        // Assert
        assertThat(foundCourse).isNotNull();
        assertThat(foundCourse.getTeacher()).isNotNull();
        assertThat(foundCourse.getTeacher().getName()).isEqualTo("Dr. Smith");
        assertThat(foundCourse.getTeacher().getEmail()).isEqualTo("dr.smith@university.edu");
    }

    @Test
    @DisplayName("Should handle course without teacher")
    void testCourseWithoutTeacher() {
        // Arrange
        Course courseWithoutTeacher = new Course();
        courseWithoutTeacher.setName("Self Study");
        courseWithoutTeacher.setCode("CS999");
        courseWithoutTeacher.setDepartment(testDepartment);
        courseWithoutTeacher.setTeacher(null);

        // Act
        Course savedCourse = courseRepository.save(courseWithoutTeacher);
        entityManager.flush();

        // Assert
        assertThat(savedCourse.getTeacher()).isNull();
        assertThat(savedCourse.getDepartment()).isNotNull();
    }

    // ===== Transactional Behavior Tests =====

    @Test
    @DisplayName("Should handle sequential saves")
    void testConcurrentModifications() {
        // Arrange
        Course savedCourse = entityManager.persistAndFlush(testCourse);
        Long courseId = savedCourse.getId();
        entityManager.clear();

        // Act - Simulating two users updating sequentially
        Course course1 = courseRepository.findById(courseId).orElse(null);
        course1.setName("Modified by User 1");
        courseRepository.saveAndFlush(course1);
        entityManager.clear();

        Course course2 = courseRepository.findById(courseId).orElse(null);
        course2.setName("Modified by User 2");
        courseRepository.saveAndFlush(course2);
        entityManager.clear();

        // Assert - Last save wins
        Course finalCourse = courseRepository.findById(courseId).orElse(null);
        assertThat(finalCourse).isNotNull();
        assertThat(finalCourse.getName()).isEqualTo("Modified by User 2");
    }

    @Test
    @DisplayName("Should count courses correctly")
    void testCount() {
        // Arrange
        entityManager.persist(testCourse);

        Course course2 = new Course();
        course2.setName("Algorithms");
        course2.setCode("CS301");
        entityManager.persist(course2);

        entityManager.flush();

        // Act
        long count = courseRepository.count();

        // Assert
        assertThat(count).isEqualTo(2);
    }
}
