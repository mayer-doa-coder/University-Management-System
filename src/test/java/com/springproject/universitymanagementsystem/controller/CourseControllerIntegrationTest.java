package com.springproject.universitymanagementsystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.springproject.universitymanagementsystem.entity.Course;
import com.springproject.universitymanagementsystem.entity.Department;
import com.springproject.universitymanagementsystem.entity.Teacher;
import com.springproject.universitymanagementsystem.service.CourseService;
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
 * Integration tests for CourseController
 * Uses @SpringBootTest and @AutoConfigureMockMvc
 * Tests HTTP status codes, request/response bodies, validation, and security
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("CourseController Integration Tests")
class CourseControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CourseService courseService;

    private Course testCourse;
    private Department testDepartment;
    private Teacher testTeacher;

    @BeforeEach
    void setUp() {
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

    // ===== GET /api/courses Tests =====

    @Test
    @WithMockUser(roles = "STUDENT")
    @DisplayName("GET /api/courses - Should return all courses with 200 OK")
    void testGetAllCourses_Success() throws Exception {
        // Arrange
        Course course2 = new Course();
        course2.setId(2L);
        course2.setName("Algorithms");
        course2.setCode("CS301");
        course2.setCredits(4);

        List<Course> courses = Arrays.asList(testCourse, course2);
        when(courseService.findAll()).thenReturn(courses);

        // Act & Assert
        mockMvc.perform(get("/api/courses")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Data Structures")))
                .andExpect(jsonPath("$[0].code", is("CS201")))
                .andExpect(jsonPath("$[0].credits", is(3)))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("Algorithms")));
    }

    @Test
    @WithMockUser(roles = "TEACHER")
    @DisplayName("GET /api/courses - Should return empty array when no courses exist")
    void testGetAllCourses_EmptyList() throws Exception {
        // Arrange
        when(courseService.findAll()).thenReturn(Arrays.asList());

        // Act & Assert
        mockMvc.perform(get("/api/courses"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    // ===== GET /api/courses/{id} Tests =====

    @Test
    @WithMockUser(roles = "STUDENT")
    @DisplayName("GET /api/courses/{id} - Should return course by ID with 200 OK")
    void testGetCourseById_Success() throws Exception {
        // Arrange
        when(courseService.findById(1L)).thenReturn(testCourse);

        // Act & Assert
        mockMvc.perform(get("/api/courses/{id}", 1L))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Data Structures")))
                .andExpect(jsonPath("$.code", is("CS201")))
                .andExpect(jsonPath("$.credits", is(3)));
    }

    @Test
    @WithMockUser(roles = "TEACHER")
    @DisplayName("GET /api/courses/{id} - Should return 404 when course not found")
    void testGetCourseById_NotFound() throws Exception {
        // Arrange
        when(courseService.findById(999L)).thenReturn(null);

        // Act & Assert
        mockMvc.perform(get("/api/courses/{id}", 999L))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    // ===== POST /api/courses Tests =====

    @Test
    @WithMockUser(roles = "TEACHER")
    @DisplayName("POST /api/courses - Should create course with TEACHER role")
    void testCreateCourse_AsTeacher_Success() throws Exception {
        // Arrange
        Course newCourse = new Course();
        newCourse.setId(3L);
        newCourse.setName("Database Systems");
        newCourse.setCode("CS202");
        newCourse.setCredits(4);

        when(courseService.save(any(Course.class))).thenReturn(newCourse);

        // Act & Assert
        mockMvc.perform(post("/api/courses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newCourse)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(3)))
                .andExpect(jsonPath("$.name", is("Database Systems")))
                .andExpect(jsonPath("$.code", is("CS202")))
                .andExpect(jsonPath("$.credits", is(4)));
    }

    @Test
    @WithMockUser(roles = "STUDENT")
    @DisplayName("POST /api/courses - Should return 403 when STUDENT tries to create")
    void testCreateCourse_AsStudent_Forbidden() throws Exception {
        // Arrange
        Course newCourse = new Course();
        newCourse.setName("Operating Systems");
        newCourse.setCode("CS302");

        // Act & Assert
        mockMvc.perform(post("/api/courses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newCourse)))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("POST /api/courses - Should return 401 when not authenticated")
    void testCreateCourse_Unauthorized() throws Exception {
        // Arrange
        Course newCourse = new Course();
        newCourse.setName("Networks");

        // Act & Assert
        mockMvc.perform(post("/api/courses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newCourse)))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    // ===== PUT /api/courses/{id} Tests =====

    @Test
    @WithMockUser(roles = "TEACHER")
    @DisplayName("PUT /api/courses/{id} - Should update course as TEACHER")
    void testUpdateCourse_AsTeacher_Success() throws Exception {
        // Arrange
        Course updatedCourse = new Course();
        updatedCourse.setId(1L);
        updatedCourse.setName("Advanced Data Structures");
        updatedCourse.setCode("CS401");
        updatedCourse.setCredits(4);

        when(courseService.update(anyLong(), any(Course.class)))
                .thenReturn(updatedCourse);

        // Act & Assert
        mockMvc.perform(put("/api/courses/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedCourse)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Advanced Data Structures")))
                .andExpect(jsonPath("$.code", is("CS401")))
                .andExpect(jsonPath("$.credits", is(4)));
    }

    @Test
    @WithMockUser(roles = "TEACHER")
    @DisplayName("PUT /api/courses/{id} - Should return 404 when course not found")
    void testUpdateCourse_NotFound() throws Exception {
        // Arrange
        Course updateRequest = new Course();
        when(courseService.update(anyLong(), any(Course.class))).thenReturn(null);

        // Act & Assert
        mockMvc.perform(put("/api/courses/{id}", 999L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "STUDENT")
    @DisplayName("PUT /api/courses/{id} - Should return 403 when STUDENT tries to update")
    void testUpdateCourse_AsStudent_Forbidden() throws Exception {
        // Arrange
        Course updateRequest = new Course();
        updateRequest.setName("Updated Course");

        // Act & Assert
        mockMvc.perform(put("/api/courses/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    // ===== DELETE /api/courses/{id} Tests =====

    @Test
    @WithMockUser(roles = "TEACHER")
    @DisplayName("DELETE /api/courses/{id} - Should delete course as TEACHER")
    void testDeleteCourse_AsTeacher_Success() throws Exception {
        // Arrange
        doNothing().when(courseService).delete(1L);

        // Act & Assert
        mockMvc.perform(delete("/api/courses/{id}", 1L))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "STUDENT")
    @DisplayName("DELETE /api/courses/{id} - Should return 403 when STUDENT tries to delete")
    void testDeleteCourse_AsStudent_Forbidden() throws Exception {
        // Act & Assert
        mockMvc.perform(delete("/api/courses/{id}", 1L))
                .andDo(print())
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("DELETE /api/courses/{id} - Should return 401 when not authenticated")
    void testDeleteCourse_Unauthorized() throws Exception {
        // Act & Assert
        mockMvc.perform(delete("/api/courses/{id}", 1L))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }
}
