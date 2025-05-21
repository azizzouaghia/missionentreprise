package com.dto;

import com.entity.Student;
import lombok.Data;

import java.util.Set;
import java.util.stream.Collectors;

@Data
public class StudentDTO {
    private Long id;
    private String username;
    private String email;
    private String matricule;
    private String niveau;
    private Set<Long> projectIds;

    public static StudentDTO fromEntity(Student student) {
        StudentDTO dto = new StudentDTO();
        dto.setId(student.getId());
        dto.setUsername(student.getUsername());
        dto.setEmail(student.getEmail());
        dto.setMatricule(student.getMatricule());
        dto.setNiveau(student.getNiveau());
        dto.setProjectIds(student.getProjects().stream()
                .map(project -> project.getId())
                .collect(Collectors.toSet()));
        return dto;
    }

    public Student toEntity() {
        Student student = new Student();
        student.setId(this.id);
        student.setUsername(this.username);
        student.setEmail(this.email);
        student.setMatricule(this.matricule);
        student.setNiveau(this.niveau);
        return student;
    }
}