package com.esprit.tn.aziz_zouaghia_tpfoyer.service;
import com.esprit.tn.aziz_zouaghia_tpfoyer.entity.User;
import com.esprit.tn.aziz_zouaghia_tpfoyer.entity.Project;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GitLabService {
    private final RestTemplate restTemplate;
    private final String gitLabApiUrl;

    @Value("${gitlab.api.url}")
    private String gitlabApiUrl;
    
    @Value("${gitlab.api.token}")
    private String privateToken;

    public GitLabService(RestTemplate restTemplate, @Value("${gitlab.api.url}") String gitLabApiUrl) {
        this.restTemplate = restTemplate;
        this.gitLabApiUrl = gitLabApiUrl;
    }

    // 1. Fetch all projects
    public ResponseEntity<List<Map<String, Object>>> fetchAllProjects() {
        HttpHeaders headers = createHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);
        
        return restTemplate.exchange(
            gitlabApiUrl + "/projects?owned=true",
            HttpMethod.GET,
            entity,
            new ParameterizedTypeReference<List<Map<String, Object>>>() {}
        );
    }

    // 2. Create a new project
    public ResponseEntity<Map<String, Object>> createProject(String name, String description) {
        HttpHeaders headers = createHeaders();
        
        Map<String, String> body = new HashMap<>();
        body.put("name", name);
        body.put("description", description);
        body.put("visibility", "private");
        
        HttpEntity<Map<String, String>> entity = new HttpEntity<>(body, headers);
        
        return restTemplate.exchange(
            gitlabApiUrl + "/projects",
            HttpMethod.POST,
            entity,
            new ParameterizedTypeReference<Map<String, Object>>() {}
        );
    }

    // 3. Fetch commits for a specific project
    public ResponseEntity<List<Map<String, Object>>> fetchProjectCommits(String projectId) {
        HttpHeaders headers = createHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);
        
        return restTemplate.exchange(
            gitlabApiUrl + "/projects/" + projectId + "/repository/commits",
            HttpMethod.GET,
            entity,
            new ParameterizedTypeReference<List<Map<String, Object>>>() {}
        );
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("PRIVATE-TOKEN", privateToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }
}