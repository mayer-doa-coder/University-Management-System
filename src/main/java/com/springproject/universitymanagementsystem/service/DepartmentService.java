package com.springproject.universitymanagementsystem.service;

import com.springproject.universitymanagementsystem.entity.Department;
import com.springproject.universitymanagementsystem.repository.DepartmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    public List<Department> findAll() {
        return departmentRepository.findAll();
    }

    public Department findById(Long id) {
        return departmentRepository.findById(id).orElse(null);
    }

    public Department save(Department department) {
        return departmentRepository.save(department);
    }

    public Department update(Long id, Department department) {
        Department existing = findById(id);
        if (existing != null) {
            existing.setName(department.getName());
            existing.setDescription(department.getDescription());
            return departmentRepository.save(existing);
        }
        return null;
    }

    public void delete(Long id) {
        departmentRepository.deleteById(id);
    }
}
