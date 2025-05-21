package com.controller;

import com.dto.ProjectDTO;
import com.dto.UserResponse;
import com.entity.Role;
import com.service.ProjectService;
import com.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/professors")
public class ProfessorController {
    private final ProjectService projectService;
    private final UserService userService;

    public ProfessorController(ProjectService projectService, UserService userService) {
        this.projectService = projectService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllProfessors() {
        return ResponseEntity.ok(userService.getUsersByRole(Role.PROF));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getProfessorById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @GetMapping("/{professorId}/projects")
    public ResponseEntity<List<ProjectDTO>> getProfessorProjects(@PathVariable Long professorId) {
        List<ProjectDTO> projects = projectService.getProjectsByProfessor(professorId).stream()
                .map(ProjectDTO::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(projects);
    }
}