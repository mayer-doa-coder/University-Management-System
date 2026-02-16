package com.springproject.universitymanagementsystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springproject.universitymanagementsystem.entity.Department;
import com.springproject.universitymanagementsystem.entity.Student;
import com.springproject.universitymanagementsystem.service.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for StudentController
 * Uses @SpringBootTest and @AutoConfigureMockMvc
 * Tests HTTP status codes, request/response bodies, validation, and security
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("StudentController Integration Tests")
class StudentControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private StudentService studentService;

    private Student testStudent;
    private Department testDepartment;

    @BeforeEach
    void setUp() {
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

    // ===== GET /api/students Tests =====

    @Test
    @WithMockUser(roles = "STUDENT")
    @DisplayName("GET /api/students - Should return all students with 200 OK")
    void testGetAllStudents_Success() throws Exception {
        // Arrange
        Student student2 = new Student();
        student2.setId(2L);
        student2.setRollNumber("CS2024002");
        student2.setName("Jane Smith");
        student2.setEmail("jane.smith@university.edu");

        List<Student> students = Arrays.asList(testStudent, student2);
        when(studentService.findAll()).thenReturn(students);

        // Act & Assert
        mockMvc.perform(get("/api/students")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].rollNumber", is("CS2024001")))
                .andExpect(jsonPath("$[0].name", is("John Doe")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].rollNumber", is("CS2024002")));
    }

    @Test
    @WithMockUser(roles = "STUDENT")
    @DisplayName("GET /api/students - Should return empty array when no students exist")
    void testGetAllStudents_EmptyList() throws Exception {
        // Arrange
        when(studentService.findAll()).thenReturn(Arrays.asList());

        // Act & Assert
        mockMvc.perform(get("/api/students"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    // ===== GET /api/students/{id} Tests =====

    @Test
    @WithMockUser(roles = "STUDENT")
    @DisplayName("GET /api/students/{id} - Should return student by ID with 200 OK")
    void testGetStudentById_Success() throws Exception {
        // Arrange
        when(studentService.findById(1L)).thenReturn(testStudent);

        // Act & Assert
        mockMvc.perform(get("/api/students/{id}", 1L))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.rollNumber", is("CS2024001")))
                .andExpect(jsonPath("$.name", is("John Doe")))
                .andExpect(jsonPath("$.email", is("john.doe@university.edu")))
                .andExpect(jsonPath("$.phone", is("+1234567890")));
    }

    @Test
    @WithMockUser(roles = "STUDENT")
    @DisplayName("GET /api/students/{id} - Should return 404 when student not found")
    void testGetStudentById_NotFound() throws Exception {
        // Arrange
        when(studentService.findById(999L)).thenReturn(null);

        // Act & Assert
        mockMvc.perform(get("/api/students/{id}", 999L))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    // ===== POST /api/students Tests =====

    @Test
    @WithMockUser(roles = "TEACHER")
    @DisplayName("POST /api/students - Should create student with TEACHER role")
    void testCreateStudent_AsTeacher_Success() throws Exception {
        // Arrange
        Student newStudent = new Student();
        newStudent.setId(3L);
        newStudent.setRollNumber("CS2024003");
        newStudent.setName("Alice Johnson");
        newStudent.setEmail("alice.johnson@university.edu");

        when(studentService.save(any(Student.class))).thenReturn(newStudent);

        // Act & Assert
        mockMvc.perform(post("/api/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newStudent)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(3)))
                .andExpect(jsonPath("$.rollNumber", is("CS2024003")))
                .andExpect(jsonPath("$.name", is("Alice Johnson")));
    }

    @Test
    @WithMockUser(roles = "STUDENT")
    @DisplayName("POST /api/students - Should return 403 when STUDENT tries to create")
    void testCreateStudent_AsStudent_Forbidden() throws Exception {
        // Arrange
        Student newStudent = new Student();
        newStudent.setRollNumber("CS2024004");
        newStudent.setName("Bob Brown");

        // Act & Assert
        mockMvc.perform(post("/api/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newStudent)))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("POST /api/students - Should return 401 when not authenticated")
    void testCreateStudent_Unauthorized() throws Exception {
        // Arrange
        Student newStudent = new Student();
        newStudent.setRollNumber("CS2024005");

        // Act & Assert
        mockMvc.perform(post("/api/students")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newStudent)))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    // ===== PUT /api/students/{id}/self Tests =====

    @Test
    @WithMockUser(roles = "STUDENT")
    @DisplayName("PUT /api/students/{id}/self - Should update student's own info")
    void testUpdateSelf_AsStudent_Success() throws Exception {
        // Arrange
        Student updatedStudent = new Student();
        updatedStudent.setId(1L);
        updatedStudent.setRollNumber("CS2024001");
        updatedStudent.setName("John Updated");
        updatedStudent.setEmail("john.updated@university.edu");
        updatedStudent.setPhone("+9999999999");

        when(studentService.updateByStudent(anyLong(), any(Student.class)))
                .thenReturn(updatedStudent);

        // Act & Assert
        mockMvc.perform(put("/api/students/{id}/self", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedStudent)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("John Updated")))
                .andExpect(jsonPath("$.email", is("john.updated@university.edu")));
    }

    @Test
    @WithMockUser(roles = "STUDENT")
    @DisplayName("PUT /api/students/{id}/self - Should return 404 when student not found")
    void testUpdateSelf_NotFound() throws Exception {
        // Arrange
        Student updateRequest = new Student();
        when(studentService.updateByStudent(anyLong(), any(Student.class))).thenReturn(null);

        // Act & Assert
        mockMvc.perform(put("/api/students/{id}/self", 999L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    // ===== PUT /api/students/{id} Tests =====

    @Test
    @WithMockUser(roles = "TEACHER")
    @DisplayName("PUT /api/students/{id} - Should update student as TEACHER")
    void testUpdateStudent_AsTeacher_Success() throws Exception {
        // Arrange
        Student updatedStudent = new Student();
        updatedStudent.setId(1L);
        updatedStudent.setRollNumber("CS2024999");
        updatedStudent.setName("John Teacher Updated");

        when(studentService.updateByTeacher(anyLong(), any(Student.class)))
                .thenReturn(updatedStudent);

        // Act & Assert
        mockMvc.perform(put("/api/students/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedStudent)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rollNumber", is("CS2024999")))
                .andExpect(jsonPath("$.name", is("John Teacher Updated")));
    }

    @Test
    @WithMockUser(roles = "STUDENT")
    @DisplayName("PUT /api/students/{id} - Should return 403 when STUDENT tries to update")
    void testUpdateStudent_AsStudent_Forbidden() throws Exception {
        // Arrange
        Student updateRequest = new Student();

        // Act & Assert
        mockMvc.perform(put("/api/students/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    // ===== DELETE /api/students/{id} Tests =====

    @Test
    @WithMockUser(roles = "TEACHER")
    @DisplayName("DELETE /api/students/{id} - Should delete student as TEACHER")
    void testDeleteStudent_AsTeacher_Success() throws Exception {
        // Arrange
        doNothing().when(studentService).delete(1L);

        // Act & Assert
        mockMvc.perform(delete("/api/students/{id}", 1L))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "STUDENT")
    @DisplayName("DELETE /api/students/{id} - Should return 403 when STUDENT tries to delete")
    void testDeleteStudent_AsStudent_Forbidden() throws Exception {
        // Act & Assert
        mockMvc.perform(delete("/api/students/{id}", 1L))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("DELETE /api/students/{id} - Should return 401 when not authenticated")
    void testDeleteStudent_Unauthorized() throws Exception {
        // Act & Assert
        mockMvc.perform(delete("/api/students/{id}", 1L))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }
}
