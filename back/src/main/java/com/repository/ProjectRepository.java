package com.repository;

import com.entity.Project;
import com.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findByStudentsContaining(Student student);
    List<Project> findByProfessorId(Long professorId);
}