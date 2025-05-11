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
        try {
            ResponseEntity<Map<String, Object>> gitlabResponse = gitLabService.createProject(
                project.getTitre(),
                project.getDescription()
            );
            
            if (gitlabResponse.getStatusCode().is2xxSuccessful() && gitlabResponse.getBody() != null) {
                String gitlabProjectId = gitlabResponse.getBody().get("id").toString();
                project.setGitlabProjectId(gitlabProjectId);
                
                // Set professor if needed
                if (professorId != null) {
                    Professor professor = (Professor) userRepository.findById(professorId)
                        .orElseThrow(() -> new RuntimeException("Professor not found"));
                    // Add professor relationship if your model supports it
                }
                
                return projectRepository.save(project);
            } else {
                throw new RuntimeException("Failed to create GitLab project: " + gitlabResponse.getStatusCode());
            }
        } catch (Exception e) {
            throw new RuntimeException("Error creating project: " + e.getMessage(), e);
        }
    }
    
    public Project assignStudentsToProject(Long projectId, List<Long> studentIds) {
        Project project = projectRepository.findById(projectId).orElseThrow();
        List<Student> students = userRepository.findAllById(studentIds).stream()
            .filter(user -> user instanceof Student)
            .map(user -> (Student) user)
            .toList();
        
        project.getStudents().addAll(students);
        return projectRepository.save(project);
    }
}