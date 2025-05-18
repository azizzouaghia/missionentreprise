package com.controller;

import com.dto.ProjectDTO;
import com.service.ProjectService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/professors")
public class ProfessorController {
    private final ProjectService projectService;

    public ProfessorController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping("/{professorId}/projects")
    public ResponseEntity<List<ProjectDTO>> getProfessorProjects(@PathVariable Long professorId) {
        List<ProjectDTO> projects = projectService.getProjectsByProfessor(professorId).stream()
                .map(ProjectDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(projects);
    }
}