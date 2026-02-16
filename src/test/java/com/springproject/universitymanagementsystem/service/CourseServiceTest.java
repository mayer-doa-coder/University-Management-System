package com.springproject.universitymanagementsystem.service;

import com.springproject.universitymanagementsystem.entity.Course;
import com.springproject.universitymanagementsystem.entity.Department;
import com.springproject.universitymanagementsystem.entity.Teacher;
import com.springproject.universitymanagementsystem.repository.CourseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for CourseService
 * Uses Mockito to mock dependencies
 * Follows AAA (Arrange-Act-Assert) pattern
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("CourseService Unit Tests")
class CourseServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private CourseService courseService;

    private Course testCourse;
    private Department testDepartment;
    private Teacher testTeacher;

    @BeforeEach
    void setUp() {
        // Arrange - Set up test data
        testDepartment = new Department();
        testDepartment.setId(1L);
        testDepartment.setName("Computer Science");

        testTeacher = new Teacher();
        testTeacher.setId(1L);
        testTeacher.setName("Dr. Smith");

        testCourse = new Course();
        testCourse.setId(1L);
        testCourse.setName("Data Structures");
        testCourse.setCode("CS201");
        testCourse.setCredits(3);
        testCourse.setDepartment(testDepartment);
        testCourse.setTeacher(testTeacher);
    }

    // ===== findAll() Tests =====

    @Test
    @DisplayName("Should return all courses successfully")
    void testFindAll_Success() {
        // Arrange
        Course course2 = new Course();
        course2.setId(2L);
        course2.setName("Algorithms");
        course2.setCode("CS301");
        course2.setCredits(4);

        List<Course> expectedCourses = Arrays.asList(testCourse, course2);
        when(courseRepository.findAll()).thenReturn(expectedCourses);

        // Act
        List<Course> actualCourses = courseService.findAll();

        // Assert
        assertThat(actualCourses).isNotNull();
        assertThat(actualCourses).hasSize(2);
        assertThat(actualCourses).containsExactlyInAnyOrder(testCourse, course2);
        verify(courseRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return empty list when no courses exist")
    void testFindAll_EmptyList() {
        // Arrange
        when(courseRepository.findAll()).thenReturn(Arrays.asList());

        // Act
        List<Course> actualCourses = courseService.findAll();

        // Assert
        assertThat(actualCourses).isNotNull();
        assertThat(actualCourses).isEmpty();
        verify(courseRepository, times(1)).findAll();
    }

    // ===== findById() Tests =====

    @Test
    @DisplayName("Should find course by ID successfully")
    void testFindById_Success() {
        // Arrange
        when(courseRepository.findById(1L)).thenReturn(Optional.of(testCourse));

        // Act
        Course foundCourse = courseService.findById(1L);

        // Assert
        assertThat(foundCourse).isNotNull();
        assertThat(foundCourse.getId()).isEqualTo(1L);
        assertThat(foundCourse.getName()).isEqualTo("Data Structures");
        assertThat(foundCourse.getCode()).isEqualTo("CS201");
        assertThat(foundCourse.getCredits()).isEqualTo(3);
        verify(courseRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should return null when course not found by ID")
    void testFindById_NotFound() {
        // Arrange
        when(courseRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        Course foundCourse = courseService.findById(999L);

        // Assert
        assertThat(foundCourse).isNull();
        verify(courseRepository, times(1)).findById(999L);
    }

    // ===== save() Tests =====

    @Test
    @DisplayName("Should save course successfully")
    void testSave_Success() {
        // Arrange
        Course newCourse = new Course();
        newCourse.setName("Database Systems");
        newCourse.setCode("CS202");
        newCourse.setCredits(4);

        when(courseRepository.save(any(Course.class))).thenReturn(newCourse);

        // Act
        Course savedCourse = courseService.save(newCourse);

        // Assert
        assertThat(savedCourse).isNotNull();
        assertThat(savedCourse.getName()).isEqualTo("Database Systems");
        assertThat(savedCourse.getCode()).isEqualTo("CS202");
        assertThat(savedCourse.getCredits()).isEqualTo(4);
        verify(courseRepository, times(1)).save(newCourse);
    }

    @Test
    @DisplayName("Should save course with null credits")
    void testSave_NullCredits() {
        // Arrange
        Course newCourse = new Course();
        newCourse.setName("Special Topics");
        newCourse.setCode("CS499");
        newCourse.setCredits(null);

        when(courseRepository.save(any(Course.class))).thenReturn(newCourse);

        // Act
        Course savedCourse = courseService.save(newCourse);

        // Assert
        assertThat(savedCourse).isNotNull();
        assertThat(savedCourse.getCredits()).isNull();
        verify(courseRepository, times(1)).save(newCourse);
    }

    // ===== update() Tests =====

    @Test
    @DisplayName("Should update course successfully")
    void testUpdate_Success() {
        // Arrange
        Department newDepartment = new Department();
        newDepartment.setId(2L);
        newDepartment.setName("Mathematics");

        Teacher newTeacher = new Teacher();
        newTeacher.setId(2L);
        newTeacher.setName("Dr. Johnson");

        Course updateRequest = new Course();
        updateRequest.setName("Advanced Data Structures");
        updateRequest.setCode("CS401");
        updateRequest.setCredits(4);
        updateRequest.setDepartment(newDepartment);
        updateRequest.setTeacher(newTeacher);

        when(courseRepository.findById(1L)).thenReturn(Optional.of(testCourse));
        when(courseRepository.save(any(Course.class))).thenReturn(testCourse);

        // Act
        Course updatedCourse = courseService.update(1L, updateRequest);

        // Assert
        assertThat(updatedCourse).isNotNull();
        assertThat(updatedCourse.getName()).isEqualTo("Advanced Data Structures");
        assertThat(updatedCourse.getCode()).isEqualTo("CS401");
        assertThat(updatedCourse.getCredits()).isEqualTo(4);
        assertThat(updatedCourse.getDepartment().getId()).isEqualTo(2L);
        assertThat(updatedCourse.getTeacher().getId()).isEqualTo(2L);
        verify(courseRepository, times(1)).findById(1L);
        verify(courseRepository, times(1)).save(testCourse);
    }

    @Test
    @DisplayName("Should return null when updating non-existing course")
    void testUpdate_NotFound() {
        // Arrange
        Course updateRequest = new Course();
        updateRequest.setName("Updated Course");
        
        when(courseRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        Course updatedCourse = courseService.update(999L, updateRequest);

        // Assert
        assertThat(updatedCourse).isNull();
        verify(courseRepository, times(1)).findById(999L);
        verify(courseRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should handle partial update with null values")
    void testUpdate_PartialUpdate() {
        // Arrange
        Course updateRequest = new Course();
        updateRequest.setName("Updated Name");
        updateRequest.setCode(null);
        updateRequest.setCredits(null);

        when(courseRepository.findById(1L)).thenReturn(Optional.of(testCourse));
        when(courseRepository.save(any(Course.class))).thenReturn(testCourse);

        // Act
        Course updatedCourse = courseService.update(1L, updateRequest);

        // Assert
        assertThat(updatedCourse).isNotNull();
        assertThat(updatedCourse.getName()).isEqualTo("Updated Name");
        verify(courseRepository, times(1)).save(testCourse);
    }

    // ===== delete() Tests =====

    @Test
    @DisplayName("Should delete course successfully")
    void testDelete_Success() {
        // Arrange
        doNothing().when(courseRepository).deleteById(1L);

        // Act
        courseService.delete(1L);

        // Assert
        verify(courseRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Should handle deletion of non-existing course")
    void testDelete_NonExistingCourse() {
        // Arrange
        doNothing().when(courseRepository).deleteById(999L);

        // Act
        courseService.delete(999L);

        // Assert
        verify(courseRepository, times(1)).deleteById(999L);
    }
}
