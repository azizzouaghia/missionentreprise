package com.controller;

import com.dto.BulkAssignRequest;
import com.dto.ProjectDTO;
import com.entity.Project;
import com.service.ProjectService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
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

    @PostMapping("/bulk-assign")
    public ResponseEntity<List<ProjectDTO>> bulkAssignStudents(
            @RequestBody BulkAssignRequest request) {
        List<Project> projects = projectService.bulkAssignStudents(request.getProjectIds(), request.getStudentIds());
        return ResponseEntity.ok(projects.stream()
                .map(ProjectDTO::fromEntity)
                .collect(Collectors.toList()));
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

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<ProjectDTO>> getProjectsByStudent(@PathVariable Long studentId) {
        List<ProjectDTO> projects = projectService.getProjectsByStudent(studentId).stream()
                .map(ProjectDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(projects);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjectDTO> updateProject(@PathVariable Long id, @RequestBody Project project) {
        Project updatedProject = projectService.updateProject(id, project);
        return ResponseEntity.ok(ProjectDTO.fromEntity(updatedProject));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        projectService.deleteProject(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{projectId}/students/{studentId}")
    public ResponseEntity<ProjectDTO> removeStudentFromProject(
            @PathVariable Long projectId,
            @PathVariable Long studentId) {
        Project project = projectService.removeStudentFromProject(projectId, studentId);
        return ResponseEntity.ok(ProjectDTO.fromEntity(project));
    }

    @GetMapping("/{id}/stats")
    public ResponseEntity<Map<String, Object>> getProjectStats(@PathVariable Long id) {
        return ResponseEntity.ok(projectService.getProjectStats(id));
    }
}