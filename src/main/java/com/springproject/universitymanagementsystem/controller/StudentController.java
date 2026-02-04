package com.springproject.universitymanagementsystem.controller;

import com.springproject.universitymanagementsystem.entity.Student;
import com.springproject.universitymanagementsystem.service.StudentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public List<Student> getAll() {
        return studentService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Student> getById(@PathVariable Long id) {
        Student student = studentService.findById(id);
        if (student != null) {
            return ResponseEntity.ok(student);
        }
        return ResponseEntity.notFound().build();
    }

    // Only teachers can create students
    @PostMapping
    @PreAuthorize("hasRole('TEACHER')")
    public Student create(@RequestBody Student student) {
        return studentService.save(student);
    }

    // Student can update their own info (except roll number)
    @PutMapping("/{id}/self")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<Student> updateSelf(@PathVariable Long id, @RequestBody Student student) {
        Student updated = studentService.updateByStudent(id, student);
        if (updated != null) {
            return ResponseEntity.ok(updated);
        }
        return ResponseEntity.notFound().build();
    }

    // Teacher can update any student info including roll number
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<Student> update(@PathVariable Long id, @RequestBody Student student) {
        Student updated = studentService.updateByTeacher(id, student);
        if (updated != null) {
            return ResponseEntity.ok(updated);
        }
        return ResponseEntity.notFound().build();
    }

    // Only teachers can delete students
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        studentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
