package com.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Base64;
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

    // --- NEW: Method to fetch repository tree ---
    public ResponseEntity<List<Map<String, Object>>> fetchRepositoryTree(String projectId, String ref, String path) {
        HttpHeaders headers = createHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);
        String url = UriComponentsBuilder.fromHttpUrl(gitLabApiUrl + "/projects/" + projectId + "/repository/tree")
                .queryParam("ref", ref)
                .queryParam("path", path)
                .toUriString();
        
        return restTemplate.exchange(
            url,
            HttpMethod.GET,
            entity,
            new ParameterizedTypeReference<>() {}
        );
    }
    
    // --- NEW: Method to fetch and decode file content ---
    public ResponseEntity<Map<String, Object>> fetchFileContent(String projectId, String ref, String filePath) {
        HttpHeaders headers = createHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);
        String url = UriComponentsBuilder.fromHttpUrl(gitLabApiUrl + "/projects/" + projectId + "/repository/files/" + filePath.replace("/", "%2F"))
                .queryParam("ref", ref)
                .toUriString();
        
        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
            url,
            HttpMethod.GET,
            entity,
            new ParameterizedTypeReference<>() {}
        );

        // Decode the base64 content for the frontend
        Map<String, Object> body = response.getBody();
        if (body != null && body.containsKey("content")) {
            String encodedContent = (String) body.get("content");
            byte[] decodedBytes = Base64.getDecoder().decode(encodedContent);
            String decodedContent = new String(decodedBytes);
            body.put("content", decodedContent); // Replace encoded with decoded
        }
        
        return ResponseEntity.ok(body);
    }
    
    public ResponseEntity<Map<String, Object>> fetchSingleCommit(String projectId, String commitSha) {
        HttpHeaders headers = createHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);

        String metaUrl = gitLabApiUrl + "/projects/" + projectId + "/repository/commits/" + commitSha;
        ResponseEntity<Map<String, Object>> metaResponse = restTemplate.exchange(
            metaUrl, HttpMethod.GET, entity, new ParameterizedTypeReference<>() {});

        String diffUrl = gitLabApiUrl + "/projects/" + projectId + "/repository/commits/" + commitSha + "/diff";
        ResponseEntity<List<Map<String, Object>>> diffResponse = restTemplate.exchange(
            diffUrl, HttpMethod.GET, entity, new ParameterizedTypeReference<>() {});

        Map<String, Object> combinedResponse = metaResponse.getBody();
        if (combinedResponse != null) {
            combinedResponse.put("diff", diffResponse.getBody());
        }

        return ResponseEntity.ok(combinedResponse);
    }
    
    // ... other existing methods ...
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