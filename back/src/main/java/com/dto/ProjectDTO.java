package com.dto;

import com.entity.Project;
import com.entity.Student;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

@Data
public class ProjectDTO {
    private Long id;
    private String titre;
    private String description;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private String gitlabProjectId;
    private Long professorId;
    private Set<Long> studentIds;

    public static ProjectDTO fromEntity(Project project) {
        ProjectDTO dto = new ProjectDTO();
        dto.setId(project.getId());
        dto.setTitre(project.getTitre());
        dto.setDescription(project.getDescription());
        dto.setDateDebut(project.getDateDebut());
        dto.setDateFin(project.getDateFin());
        dto.setGitlabProjectId(project.getGitlabProjectId());
        dto.setProfessorId(project.getProfessor() != null ? project.getProfessor().getId() : null);
        dto.setStudentIds(project.getStudents().stream()
                .map(Student::getId)
                .collect(Collectors.toSet()));
        return dto;
    }
}