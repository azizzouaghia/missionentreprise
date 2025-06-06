package com.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GitLabService {
    private final RestTemplate restTemplate;
    
    @Value("${gitlab.api.url}")
    private String gitLabApiUrl;
    
    @Value("${gitlab.api.token}")
    private String privateToken;

    public GitLabService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ResponseEntity<List<Map<String, Object>>> fetchAllProjects() {
        HttpHeaders headers = createHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);
        
        return restTemplate.exchange(
            gitLabApiUrl + "/projects?owned=true",
            HttpMethod.GET,
            entity,
            new ParameterizedTypeReference<List<Map<String, Object>>>() {}
        );
    }

    public Integer getCommitCount(String projectId) {
        HttpHeaders headers = createHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);
        
        ResponseEntity<List<Map<String, Object>>> commitsResponse = restTemplate.exchange(
            gitLabApiUrl + "/projects/" + projectId + "/repository/commits",
            HttpMethod.GET,
            entity,
            new ParameterizedTypeReference<List<Map<String, Object>>>() {}
        );
        
        if (commitsResponse.getBody() != null) {
            return commitsResponse.getBody().size();
        }
        return 0;
    }

    public ResponseEntity<Map<String, Object>> createProject(String name, String description) {
        HttpHeaders headers = createHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        Map<String, String> body = new HashMap<>();
        body.put("name", name);
        body.put("description", description);
        body.put("visibility", "private");

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(body, headers);

        try {
            return restTemplate.exchange(
                gitLabApiUrl + "/projects",
                HttpMethod.POST,
                entity,
                new ParameterizedTypeReference<Map<String, Object>>() {}
            );
        } catch (HttpClientErrorException e) {
            throw new RuntimeException("GitLab API error: " + e.getResponseBodyAsString(), e);
        } catch (Exception e) {
            throw new RuntimeException("Failed to call GitLab API", e);
        }
    }

    public ResponseEntity<List<Map<String, Object>>> fetchProjectCommits(String projectId) {
        HttpHeaders headers = createHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);
        
        return restTemplate.exchange(
            gitLabApiUrl + "/projects/" + projectId + "/repository/commits",
            HttpMethod.GET,
            entity,
            new ParameterizedTypeReference<List<Map<String, Object>>>() {}
        );
    }

    public ResponseEntity<List<Map<String, Object>>> fetchProjectBranches(String projectId) {
        HttpHeaders headers = createHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);
        
        return restTemplate.exchange(
            gitLabApiUrl + "/projects/" + projectId + "/repository/branches",
            HttpMethod.GET,
            entity,
            new ParameterizedTypeReference<List<Map<String, Object>>>() {}
        );
    }

    public ResponseEntity<List<Map<String, Object>>> fetchProjectMergeRequests(String projectId) {
        HttpHeaders headers = createHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);
        
        return restTemplate.exchange(
            gitLabApiUrl + "/projects/" + projectId + "/merge_requests",
            HttpMethod.GET,
            entity,
            new ParameterizedTypeReference<List<Map<String, Object>>>() {}
        );
    }

    public ResponseEntity<Map<String, Object>> createBranch(String projectId, String branchName, String ref) {
        HttpHeaders headers = createHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        Map<String, String> body = new HashMap<>();
        body.put("branch", branchName);
        body.put("ref", ref);

        HttpEntity<Map<String, String>> entity = new HttpEntity<>(body, headers);

        try {
            return restTemplate.exchange(
                gitLabApiUrl + "/projects/" + projectId + "/repository/branches",
                HttpMethod.POST,
                entity,
                new ParameterizedTypeReference<Map<String, Object>>() {}
            );
        } catch (HttpClientErrorException e) {
            throw new RuntimeException("GitLab API error: " + e.getResponseBodyAsString(), e);
        } catch (Exception e) {
            throw new RuntimeException("Failed to call GitLab API", e);
        }
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("PRIVATE-TOKEN", privateToken);
        return headers;
    }
}