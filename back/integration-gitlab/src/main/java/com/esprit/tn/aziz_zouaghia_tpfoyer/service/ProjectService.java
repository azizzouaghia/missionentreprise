package com.esprit.tn.aziz_zouaghia_tpfoyer.service;

import com.esprit.tn.aziz_zouaghia_tpfoyer.entity.Professor;
import com.esprit.tn.aziz_zouaghia_tpfoyer.entity.Project;
import com.esprit.tn.aziz_zouaghia_tpfoyer.entity.Student;
import com.esprit.tn.aziz_zouaghia_tpfoyer.repository.ProjectRepository;
import com.esprit.tn.aziz_zouaghia_tpfoyer.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final GitLabService gitLabService;

    public ProjectService(ProjectRepository projectRepository, 
                         UserRepository userRepository,
                         GitLabService gitLabService) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.gitLabService = gitLabService;
    }

    public Project createProject(Project project, Long professorId) {
        Professor professor = (Professor) userRepository.findById(professorId)
            .orElseThrow(() -> new RuntimeException("Professor not found"));
        
        // Call GitLabService.createProject with name and description
        ResponseEntity<Map<String, Object>> response = gitLabService.createProject(
            project.getTitre(),
            project.getDescription()
        );
        
        // Parse GitLab project ID from response
        String gitlabProjectId = parseGitLabProjectId(response.getBody());
        project.setGitlabProjectId(gitlabProjectId);
        
        return projectRepository.save(project);
    }
    
    private String parseGitLabProjectId(Map<String, Object> responseBody) {
        // Extract the project ID from the GitLab response
        Object id = responseBody.get("id");
        return id != null ? id.toString() : "unknown";
    }
    
    public Project assignStudentsToProject(Long projectId, List<Long> studentIds) {
        Project project = projectRepository.findById(projectId)
            .orElseThrow(() -> new RuntimeException("Project not found"));
        
        List<Student> students = userRepository.findAllById(studentIds).stream()
            .filter(user -> user instanceof Student)
            .map(user -> (Student) user)
            .toList();
        
        project.getStudents().addAll(students);
        return projectRepository.save(project);
    }
}