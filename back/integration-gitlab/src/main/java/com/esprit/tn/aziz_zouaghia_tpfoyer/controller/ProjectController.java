package com.esprit.tn.aziz_zouaghia_tpfoyer.controller;
import com.esprit.tn.aziz_zouaghia_tpfoyer.entity.Project;
import com.esprit.tn.aziz_zouaghia_tpfoyer.service.ProjectService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {
    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping
    public ResponseEntity<Project> createProject(
            @RequestBody Project project,
            @RequestParam Long professorId) {
        // No JWT check - just pass through
        return ResponseEntity.ok(projectService.createProject(project, professorId));
    }

    @PostMapping("/{projectId}/assign")
    public ResponseEntity<Project> assignStudents(
            @PathVariable Long projectId,
            @RequestBody List<Long> studentIds) {
        // No JWT check - just pass through
        return ResponseEntity.ok(projectService.assignStudentsToProject(projectId, studentIds));
    }
}