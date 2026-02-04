package com.springproject.universitymanagementsystem.repository;

import com.springproject.universitymanagementsystem.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    Student findByRollNumber(String rollNumber);
}
