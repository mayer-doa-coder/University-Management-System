package com.springproject.universitymanagementsystem.service;

import com.springproject.universitymanagementsystem.entity.Course;
import com.springproject.universitymanagementsystem.entity.Department;
import com.springproject.universitymanagementsystem.entity.Student;
import com.springproject.universitymanagementsystem.repository.StudentRepository;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

/**
 * Unit tests for StudentService
 * Uses Mockito to mock dependencies
 * Follows AAA (Arrange-Act-Assert) pattern
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("StudentService Unit Tests")
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentService studentService;

    private Student testStudent;
    private Department testDepartment;

    @BeforeEach
    void setUp() {
        // Arrange - Set up test data
        testDepartment = new Department();
        testDepartment.setId(1L);
        testDepartment.setName("Computer Science");

        testStudent = new Student();
        testStudent.setId(1L);
        testStudent.setRollNumber("CS2024001");
        testStudent.setName("John Doe");
        testStudent.setEmail("john.doe@university.edu");
        testStudent.setPhone("+1234567890");
        testStudent.setDepartment(testDepartment);
    }

    // ===== findAll() Tests =====

    @Test
    @DisplayName("Should return all students successfully")
    void testFindAll_Success() {
        // Arrange
        Student student2 = new Student();
        student2.setId(2L);
        student2.setRollNumber("CS2024002");
        student2.setName("Jane Smith");
        
        List<Student> expectedStudents = Arrays.asList(testStudent, student2);
        when(studentRepository.findAll()).thenReturn(expectedStudents);

        // Act
        List<Student> actualStudents = studentService.findAll();

        // Assert
        assertThat(actualStudents).isNotNull();
        assertThat(actualStudents).hasSize(2);
        assertThat(actualStudents).containsExactlyInAnyOrder(testStudent, student2);
        verify(studentRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return empty list when no students exist")
    void testFindAll_EmptyList() {
        // Arrange
        when(studentRepository.findAll()).thenReturn(Arrays.asList());

        // Act
        List<Student> actualStudents = studentService.findAll();

        // Assert
        assertThat(actualStudents).isNotNull();
        assertThat(actualStudents).isEmpty();
        verify(studentRepository, times(1)).findAll();
    }

    // ===== findById() Tests =====

    @Test
    @DisplayName("Should find student by ID successfully")
    void testFindById_Success() {
        // Arrange
        when(studentRepository.findById(1L)).thenReturn(Optional.of(testStudent));

        // Act
        Student foundStudent = studentService.findById(1L);

        // Assert
        assertThat(foundStudent).isNotNull();
        assertThat(foundStudent.getId()).isEqualTo(1L);
        assertThat(foundStudent.getRollNumber()).isEqualTo("CS2024001");
        assertThat(foundStudent.getName()).isEqualTo("John Doe");
        verify(studentRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should return null when student not found by ID")
    void testFindById_NotFound() {
        // Arrange
        when(studentRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        Student foundStudent = studentService.findById(999L);

        // Assert
        assertThat(foundStudent).isNull();
        verify(studentRepository, times(1)).findById(999L);
    }

    // ===== findByRollNumber() Tests =====

    @Test
    @DisplayName("Should find student by roll number successfully")
    void testFindByRollNumber_Success() {
        // Arrange
        when(studentRepository.findByRollNumber("CS2024001")).thenReturn(testStudent);

        // Act
        Student foundStudent = studentService.findByRollNumber("CS2024001");

        // Assert
        assertThat(foundStudent).isNotNull();
        assertThat(foundStudent.getRollNumber()).isEqualTo("CS2024001");
        verify(studentRepository, times(1)).findByRollNumber("CS2024001");
    }

    @Test
    @DisplayName("Should return null when student not found by roll number")
    void testFindByRollNumber_NotFound() {
        // Arrange
        when(studentRepository.findByRollNumber("INVALID")).thenReturn(null);

        // Act
        Student foundStudent = studentService.findByRollNumber("INVALID");

        // Assert
        assertThat(foundStudent).isNull();
        verify(studentRepository, times(1)).findByRollNumber("INVALID");
    }

    // ===== save() Tests =====

    @Test
    @DisplayName("Should save student successfully")
    void testSave_Success() {
        // Arrange
        Student newStudent = new Student();
        newStudent.setRollNumber("CS2024003");
        newStudent.setName("Alice Johnson");
        
        when(studentRepository.save(any(Student.class))).thenReturn(newStudent);

        // Act
        Student savedStudent = studentService.save(newStudent);

        // Assert
        assertThat(savedStudent).isNotNull();
        assertThat(savedStudent.getRollNumber()).isEqualTo("CS2024003");
        verify(studentRepository, times(1)).save(newStudent);
    }

    // ===== updateByStudent() Tests =====

    @Test
    @DisplayName("Should update student info by student (excluding roll number)")
    void testUpdateByStudent_Success() {
        // Arrange
        Student updateRequest = new Student();
        updateRequest.setRollNumber("CHANGED"); // This should be ignored
        updateRequest.setName("John Updated");
        updateRequest.setEmail("john.updated@university.edu");
        updateRequest.setPhone("+9876543210");

        when(studentRepository.findById(1L)).thenReturn(Optional.of(testStudent));
        when(studentRepository.save(any(Student.class))).thenReturn(testStudent);

        // Act
        Student updatedStudent = studentService.updateByStudent(1L, updateRequest);

        // Assert
        assertThat(updatedStudent).isNotNull();
        assertThat(updatedStudent.getRollNumber()).isEqualTo("CS2024001"); // Roll number unchanged
        assertThat(updatedStudent.getName()).isEqualTo("John Updated");
        assertThat(updatedStudent.getEmail()).isEqualTo("john.updated@university.edu");
        assertThat(updatedStudent.getPhone()).isEqualTo("+9876543210");
        verify(studentRepository, times(1)).findById(1L);
        verify(studentRepository, times(1)).save(testStudent);
    }

    @Test
    @DisplayName("Should return null when updating non-existing student by student")
    void testUpdateByStudent_NotFound() {
        // Arrange
        Student updateRequest = new Student();
        when(studentRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        Student updatedStudent = studentService.updateByStudent(999L, updateRequest);

        // Assert
        assertThat(updatedStudent).isNull();
        verify(studentRepository, times(1)).findById(999L);
        verify(studentRepository, never()).save(any());
    }

    // ===== updateByTeacher() Tests =====

    @Test
    @DisplayName("Should update all student info by teacher (including roll number)")
    void testUpdateByTeacher_Success() {
        // Arrange
        Department newDepartment = new Department();
        newDepartment.setId(2L);
        newDepartment.setName("Mathematics");

        Student updateRequest = new Student();
        updateRequest.setRollNumber("CS2024999");
        updateRequest.setName("John Teacher Updated");
        updateRequest.setEmail("john.teacher@university.edu");
        updateRequest.setPhone("+1111111111");
        updateRequest.setDepartment(newDepartment);
        updateRequest.setCourses(Arrays.asList());

        when(studentRepository.findById(1L)).thenReturn(Optional.of(testStudent));
        when(studentRepository.save(any(Student.class))).thenReturn(testStudent);

        // Act
        Student updatedStudent = studentService.updateByTeacher(1L, updateRequest);

        // Assert
        assertThat(updatedStudent).isNotNull();
        assertThat(updatedStudent.getRollNumber()).isEqualTo("CS2024999"); // Roll number changed
        assertThat(updatedStudent.getName()).isEqualTo("John Teacher Updated");
        assertThat(updatedStudent.getEmail()).isEqualTo("john.teacher@university.edu");
        assertThat(updatedStudent.getDepartment().getId()).isEqualTo(2L);
        verify(studentRepository, times(1)).findById(1L);
        verify(studentRepository, times(1)).save(testStudent);
    }

    @Test
    @DisplayName("Should return null when updating non-existing student by teacher")
    void testUpdateByTeacher_NotFound() {
        // Arrange
        Student updateRequest = new Student();
        when(studentRepository.findById(999L)).thenReturn(Optional.empty());

        // Act
        Student updatedStudent = studentService.updateByTeacher(999L, updateRequest);

        // Assert
        assertThat(updatedStudent).isNull();
        verify(studentRepository, times(1)).findById(999L);
        verify(studentRepository, never()).save(any());
    }

    // ===== delete() Tests =====

    @Test
    @DisplayName("Should delete student successfully")
    void testDelete_Success() {
        // Arrange
        doNothing().when(studentRepository).deleteById(1L);

        // Act
        studentService.delete(1L);

        // Assert
        verify(studentRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Should handle deletion of non-existing student")
    void testDelete_NonExistingStudent() {
        // Arrange
        doNothing().when(studentRepository).deleteById(999L);

        // Act
        studentService.delete(999L);

        // Assert
        verify(studentRepository, times(1)).deleteById(999L);
    }
}
