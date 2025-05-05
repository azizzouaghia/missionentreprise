package com.esprit.tn.aziz_zouaghia_tpfoyer.service;
import com.esprit.tn.aziz_zouaghia_tpfoyer.entity.User;
import com.esprit.tn.aziz_zouaghia_tpfoyer.entity.Project;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class GitLabService {
    private final RestTemplate restTemplate;
    
    @Value("${gitlab.api.url}")
    private String gitlabApiUrl;
    
    @Value("${gitlab.private.token}")
    private String privateToken;
    
    public GitLabService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    
    public ResponseEntity<String> createGitLabUser(User user) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("PRIVATE-TOKEN", privateToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        Map<String, Object> body = Map.of(
            "email", user.getEmail(),
            "username", user.getUsername(),
            "name", user.getUsername(),
            "password", "temporaryPassword123",
            "skip_confirmation", true
        );
        
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
        return restTemplate.postForEntity(gitlabApiUrl + "/users", entity, String.class);
    }

    public ResponseEntity<List<Object>> getProjectCommits(String projectId) {
    HttpHeaders headers = new HttpHeaders();
    headers.set("PRIVATE-TOKEN", privateToken);
    
    HttpEntity<String> entity = new HttpEntity<>(headers);
    return restTemplate.exchange(
        gitlabApiUrl + "/projects/" + projectId + "/repository/commits",
        HttpMethod.GET,
        entity,
        new ParameterizedTypeReference<List<Object>>() {}
    );
}

public ResponseEntity<String> createGitLabProject(Project project) {
    HttpHeaders headers = new HttpHeaders();
    headers.set("PRIVATE-TOKEN", privateToken);
    headers.setContentType(MediaType.APPLICATION_JSON);
    
    Map<String, Object> body = Map.of(
        "name", project.getTitre(),
        "description", project.getDescription(),
        "visibility", "private"
    );
    
    HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
    return restTemplate.postForEntity(gitlabApiUrl + "/projects", entity, String.class);
}
}