package com.controller;

import com.entity.User;
import com.service.GitLabService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/gitlab")
@RequiredArgsConstructor
public class GitLabController {
    private final GitLabService gitLabService;

    @GetMapping("/projects")
    public ResponseEntity<List<Map<String, Object>>> getAllProjects() {
        return gitLabService.fetchAllProjects();
    }

    @PostMapping("/projects")
    public ResponseEntity<Map<String, Object>> createProject(
            @RequestParam String name,
            @RequestParam String description) {
        return gitLabService.createProject(name, description);
    }

    @GetMapping("/projects/{projectId}/commits")
    public ResponseEntity<List<Map<String, Object>>> getProjectCommits(
            @PathVariable String projectId) {
        return gitLabService.fetchProjectCommits(projectId);
    }

    @GetMapping("/projects/{projectId}/branches")
    public ResponseEntity<List<Map<String, Object>>> getProjectBranches(
            @PathVariable String projectId) {
        return gitLabService.fetchProjectBranches(projectId);
    }

    @GetMapping("/projects/{projectId}/merge-requests")
    public ResponseEntity<List<Map<String, Object>>> getProjectMergeRequests(
            @PathVariable String projectId) {
        return gitLabService.fetchProjectMergeRequests(projectId);
    }

    @PostMapping("/projects/{projectId}/branches")
    public ResponseEntity<Map<String, Object>> createBranch(
            @PathVariable String projectId,
            @RequestParam String branchName,
            @RequestParam String ref) {
        return gitLabService.createBranch(projectId, branchName, ref);
    }
}