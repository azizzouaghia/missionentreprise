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

    // --- NEW: Endpoint to get the repository file tree ---
    @GetMapping("/projects/{projectId}/repository/tree")
    public ResponseEntity<List<Map<String, Object>>> getRepositoryTree(
            @PathVariable String projectId,
            @RequestParam String ref,
            @RequestParam(required = false, defaultValue = "") String path) {
        return gitLabService.fetchRepositoryTree(projectId, ref, path);
    }

    // --- NEW: Endpoint to get a single file's content ---
    @GetMapping("/projects/{projectId}/repository/files")
    public ResponseEntity<Map<String, Object>> getFileContent(
            @PathVariable String projectId,
            @RequestParam String ref,
            @RequestParam String filePath) {
        return gitLabService.fetchFileContent(projectId, ref, filePath);
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

    @GetMapping("/projects/{projectId}/commits/{commitId}")
    public ResponseEntity<Map<String, Object>> getSingleCommit(
            @PathVariable String projectId,
            @PathVariable String commitId) {
        return gitLabService.fetchSingleCommit(projectId, commitId);
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