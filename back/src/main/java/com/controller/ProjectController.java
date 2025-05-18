package com.controller;

import com.dto.ProjectDTO;
import com.entity.Project;
import com.service.ProjectService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {
    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping
    public ResponseEntity<ProjectDTO> createProject(
            @RequestBody Project project,
            @RequestParam Long professorId) {
        Project savedProject = projectService.createProject(project, professorId);
        return ResponseEntity.ok(ProjectDTO.fromEntity(savedProject));
    }

    @PostMapping("/{projectId}/assign")
    public ResponseEntity<ProjectDTO> assignStudents(
            @PathVariable Long projectId,
            @RequestBody List<Long> studentIds) {
        Project project = projectService.assignStudentsToProject(projectId, studentIds);
        return ResponseEntity.ok(ProjectDTO.fromEntity(project));
    }

    @GetMapping
    public ResponseEntity<List<ProjectDTO>> getAllProjects() {
        List<ProjectDTO> projects = projectService.getAllProjects().stream()
                .map(ProjectDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(projects);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectDTO> getProjectById(@PathVariable Long id) {
        Project project = projectService.getProjectById(id);
        return ResponseEntity.ok(ProjectDTO.fromEntity(project));
    }

    @GetMapping("/dto")
    public ResponseEntity<List<ProjectDTO>> getAllProjectsDTO() {
        List<ProjectDTO> projects = projectService.getAllProjects().stream()
                .map(ProjectDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(projects);
    }

    @GetMapping("/dto/{id}")
    public ResponseEntity<ProjectDTO> getProjectDTOById(@PathVariable Long id) {
        Project project = projectService.getProjectById(id);
        return ResponseEntity.ok(ProjectDTO.fromEntity(project));
    }
}