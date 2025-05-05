package com.esprit.tn.aziz_zouaghia_tpfoyer.controller;
import com.esprit.tn.aziz_zouaghia_tpfoyer.entity.User;
import com.esprit.tn.aziz_zouaghia_tpfoyer.service.GitLabService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/gitlab")
@RequiredArgsConstructor
public class GitLabController {
    private final GitLabService gitLabService;

    @PostMapping("/users")
    public ResponseEntity<String> createGitLabUser(@RequestBody User user) {
        return gitLabService.createGitLabUser(user);
    }

    @GetMapping("/projects/{projectId}/commits")
    public ResponseEntity<List<Object>> getProjectCommits(@PathVariable String projectId) {
        return gitLabService.getProjectCommits(projectId);
    }
}