package com.esprit.tn.aziz_zouaghia_tpfoyer.service;
import com.esprit.tn.aziz_zouaghia_tpfoyer.entity.Professor;
import com.esprit.tn.aziz_zouaghia_tpfoyer.entity.Project;
import com.esprit.tn.aziz_zouaghia_tpfoyer.entity.Student;
import com.esprit.tn.aziz_zouaghia_tpfoyer.repository.ProjectRepository;
import com.esprit.tn.aziz_zouaghia_tpfoyer.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

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
        
        ResponseEntity<String> response = gitLabService.createGitLabProject(project);
        
        String gitlabProjectId = parseGitLabProjectId(response.getBody());
        project.setGitlabProjectId(gitlabProjectId);
        
        return projectRepository.save(project);
    }
    
    private String parseGitLabProjectId(String responseBody) {
        // Implementation to parse GitLab project ID from response
        return responseBody.contains("\"id\":") ? 
               responseBody.split("\"id\":")[1].split(",")[0] : "unknown";
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