package com.springproject.universitymanagementsystem.service;

import com.springproject.universitymanagementsystem.entity.Student;
import com.springproject.universitymanagementsystem.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public List<Student> findAll() {
        return studentRepository.findAll();
    }

    public Student findById(Long id) {
        return studentRepository.findById(id).orElse(null);
    }

    public Student findByRollNumber(String rollNumber) {
        return studentRepository.findByRollNumber(rollNumber);
    }

    public Student save(Student student) {
        return studentRepository.save(student);
    }

    // Student can update their info except roll number
    public Student updateByStudent(Long id, Student student) {
        Student existing = findById(id);
        if (existing != null) {
            // Roll number cannot be changed by student
            existing.setName(student.getName());
            existing.setEmail(student.getEmail());
            existing.setPhone(student.getPhone());
            return studentRepository.save(existing);
        }
        return null;
    }

    // Teacher can update all student info including roll number
    public Student updateByTeacher(Long id, Student student) {
        Student existing = findById(id);
        if (existing != null) {
            existing.setRollNumber(student.getRollNumber());
            existing.setName(student.getName());
            existing.setEmail(student.getEmail());
            existing.setPhone(student.getPhone());
            existing.setDepartment(student.getDepartment());
            existing.setCourses(student.getCourses());
            return studentRepository.save(existing);
        }
        return null;
    }

    public void delete(Long id) {
        studentRepository.deleteById(id);
    }
}
