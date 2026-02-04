package com.springproject.universitymanagementsystem.controller;

import com.springproject.universitymanagementsystem.entity.Teacher;
import com.springproject.universitymanagementsystem.service.TeacherService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teachers")
public class TeacherController {

    private final TeacherService teacherService;

    public TeacherController(TeacherService teacherService) {
        this.teacherService = teacherService;
    }

    @GetMapping
    public List<Teacher> getAll() {
        return teacherService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Teacher> getById(@PathVariable Long id) {
        Teacher teacher = teacherService.findById(id);
        if (teacher != null) {
            return ResponseEntity.ok(teacher);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    @PreAuthorize("hasRole('TEACHER')")
    public Teacher create(@RequestBody Teacher teacher) {
        return teacherService.save(teacher);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<Teacher> update(@PathVariable Long id, @RequestBody Teacher teacher) {
        Teacher updated = teacherService.update(id, teacher);
        if (updated != null) {
            return ResponseEntity.ok(updated);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('TEACHER')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        teacherService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
