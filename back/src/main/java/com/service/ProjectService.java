package com.service;

import com.entity.PhaseStatus;
import com.entity.Professor;
import com.entity.Project;
import com.entity.Role;
import com.entity.Student;
import com.repository.FeedbackRepository;
import com.repository.PhaseRepository;
import com.repository.ProjectRepository;
import com.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
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
    private final FeedbackRepository feedbackRepository;
    private final PhaseRepository phaseRepository;

    public ProjectService(ProjectRepository projectRepository,
                          UserRepository userRepository,
                          GitLabService gitLabService,
                          FeedbackRepository feedbackRepository,
                          PhaseRepository phaseRepository) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.gitLabService = gitLabService;
        this.feedbackRepository = feedbackRepository;
        this.phaseRepository = phaseRepository;
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
                    project.setProfessor(professor);
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
        
        Set<Student> updatedStudents = new HashSet<>(project.getStudents());
        updatedStudents.addAll(students);
        project.setStudents(updatedStudents);
        
        return projectRepository.save(project);
    }

    @Transactional
    public List<Project> bulkAssignStudents(List<Long> projectIds, List<Long> studentIds) {
        Set<Student> students = studentIds.stream()
                .map(id -> userRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("User not found with id: " + id)))
                .filter(user -> user instanceof Student)
                .map(user -> (Student) user)
                .collect(Collectors.toSet());

        List<Project> projects = projectIds.stream()
                .map(id -> projectRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Project not found with id: " + id)))
                .collect(Collectors.toList());

        projects.forEach(project -> {
            Set<Student> updatedStudents = new HashSet<>(project.getStudents());
            updatedStudents.addAll(students);
            project.setStudents(updatedStudents);
            projectRepository.save(project);
        });

        return projects;
    }

    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    public Project getProjectById(Long id) {
        return projectRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Project not found with id: " + id));
    }

    public List<Project> getProjectsByProfessor(Long professorId) {
        return projectRepository.findByProfessorId(professorId);
    }

    public List<Project> getProjectsByStudent(Long studentId) {
        Student student = (Student) userRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + studentId));
        if (student.getRole() != Role.ETUDIANT) {
            throw new RuntimeException("User is not a student: " + studentId);
        }
        return projectRepository.findByStudentsContaining(student);
    }

    @Transactional
    public Project updateProject(Long id, Project projectDetails) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found with id: " + id));

        project.setTitre(projectDetails.getTitre());
        project.setDescription(projectDetails.getDescription());
        project.setDateDebut(projectDetails.getDateDebut());
        project.setDateFin(projectDetails.getDateFin());

        return projectRepository.save(project);
    }

    @Transactional
    public void deleteProject(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found with id: " + id));
        projectRepository.delete(project);
    }

    @Transactional
    public Project removeStudentFromProject(Long projectId, Long studentId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found with id: " + projectId));
        Student student = (Student) userRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + studentId));
        if (student.getRole() != Role.ETUDIANT) {
            throw new RuntimeException("User is not a student: " + studentId);
        }

        project.removeStudent(student);
        return projectRepository.save(project);
    }

    public Map<String, Object> getProjectStats(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found with id: " + projectId));

        Map<String, Object> stats = new HashMap<>();
        stats.put("commitCount", project.getGitlabProjectId() != null ?
                gitLabService.getCommitCount(project.getGitlabProjectId()) : 0);
        stats.put("averageFeedbackScore", feedbackRepository.avgNoteByProjectId(projectId) != null ?
                feedbackRepository.avgNoteByProjectId(projectId) : 0.0);
        stats.put("teamSize", project.getStudents().size());
        stats.put("phaseCount", phaseRepository.findByProjectId(projectId).size());
        Map<PhaseStatus, Long> phaseStatusCounts = phaseRepository.findByProjectId(projectId).stream()
                .collect(Collectors.groupingBy(phase -> phase.getStatus(), Collectors.counting()));
        stats.put("phasesByStatus", phaseStatusCounts);

        return stats;
    }
}