package com.springproject.universitymanagementsystem.controller;

import com.springproject.universitymanagementsystem.entity.Department;
import com.springproject.universitymanagementsystem.service.DepartmentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/departments")
public class DepartmentController {

    private final DepartmentService departmentService;

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @GetMapping
    public List<Department> getAll() {
        return departmentService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Department> getById(@PathVariable Long id) {
        Department department = departmentService.findById(id);
        if (department != null) {
            return ResponseEntity.ok(department);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public Department create(@RequestBody Department department) {
        return departmentService.save(department);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Department> update(@PathVariable Long id, @RequestBody Department department) {
        Department updated = departmentService.update(id, department);
        if (updated != null) {
            return ResponseEntity.ok(updated);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        departmentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
