package com.esprit.tn.aziz_zouaghia_tpfoyer.service;

import com.esprit.tn.aziz_zouaghia_tpfoyer.entity.Professor;
import com.esprit.tn.aziz_zouaghia_tpfoyer.entity.Project;
import com.esprit.tn.aziz_zouaghia_tpfoyer.entity.Student;
import com.esprit.tn.aziz_zouaghia_tpfoyer.repository.ProjectRepository;
import com.esprit.tn.aziz_zouaghia_tpfoyer.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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
                
                if (professorId != null) {
                    Professor professor = (Professor) userRepository.findById(professorId)
                        .orElseThrow(() -> new RuntimeException("Professor not found"));
                }
                
                return projectRepository.save(project);
            } else {
                throw new RuntimeException("Failed to create GitLab project: " + gitlabResponse.getStatusCode());
            }
        } catch (Exception e) {
            throw new RuntimeException("Error creating project: " + e.getMessage(), e);
        }
    }
    
    @Transactional
    public Project assignStudentsToProject(Long projectId, List<Long> studentIds) {
        Project project = projectRepository.findById(projectId)
            .orElseThrow(() -> new RuntimeException("Project not found with id: " + projectId));
        
        Set<Student> students = studentIds.stream()
            .map(id -> userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id)))
            .filter(user -> user instanceof Student)
            .map(user -> (Student) user)
            .collect(Collectors.toSet());
        
        // Create a new HashSet to avoid ConcurrentModificationException
        Set<Student> updatedStudents = new HashSet<>(project.getStudents());
        updatedStudents.addAll(students);
        project.setStudents(updatedStudents);
        
        return projectRepository.save(project);
    }
}