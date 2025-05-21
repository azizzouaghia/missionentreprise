package com.service;

import com.entity.PhaseStatus;
import com.entity.Project;
import com.entity.Role;
import com.entity.Student;
import com.repository.FeedbackRepository;
import com.repository.PhaseRepository;
import com.repository.ProjectRepository;
import com.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StatsService {
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final PhaseRepository phaseRepository;
    private final FeedbackRepository feedbackRepository;
    private final GitLabService gitLabService;

    public StatsService(ProjectRepository projectRepository,
                        UserRepository userRepository,
                        PhaseRepository phaseRepository,
                        FeedbackRepository feedbackRepository,
                        GitLabService gitLabService) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.phaseRepository = phaseRepository;
        this.feedbackRepository = feedbackRepository;
        this.gitLabService = gitLabService;
    }

    public Map<String, Object> getStats() {
        Map<String, Object> stats = new HashMap<>();

        // User stats
        stats.put("totalProfessors", userRepository.countByRole(Role.PROF));
        stats.put("totalStudents", userRepository.countByRole(Role.ETUDIANT));
        stats.put("totalAdmins", userRepository.countByRole(Role.ADMIN));

        // Project stats
        List<Project> projects = projectRepository.findAll();
        stats.put("totalProjects", projects.size());
        stats.put("activeProjects", projects.stream()
                .filter(p -> p.getDateFin() == null || p.getDateFin().isAfter(LocalDate.now()))
                .count());
        stats.put("completedProjects", projects.stream()
                .filter(p -> p.getDateFin() != null && p.getDateFin().isBefore(LocalDate.now()))
                .count());
        stats.put("projectCompletionRate", projects.isEmpty() ? 0.0 :
                (double) projects.stream()
                        .filter(p -> p.getDateFin() != null && p.getDateFin().isBefore(LocalDate.now()))
                        .count() / projects.size() * 100);

        // Phase stats
        stats.put("totalPhases", phaseRepository.findAll().size());
        Map<PhaseStatus, Long> phaseStatusCounts = phaseRepository.findAll().stream()
                .collect(Collectors.groupingBy(phase -> phase.getStatus(), Collectors.counting()));
        stats.put("phasesByStatus", phaseStatusCounts);

        // Feedback stats
        stats.put("averageFeedbackScore", feedbackRepository.avgNote() != null ? feedbackRepository.avgNote() : 0.0);

        // GitLab stats
        long totalCommits = projects.stream()
                .filter(p -> p.getGitlabProjectId() != null)
                .mapToInt(p -> gitLabService.getCommitCount(p.getGitlabProjectId()))
                .sum();
        stats.put("averageCommitsPerProject", projects.isEmpty() ? 0.0 : (double) totalCommits / projects.size());

        // Assignment stats
        stats.put("studentsAssigned", userRepository.findByRole(Role.ETUDIANT).stream()
                .filter(u -> !((Student) u).getProjects().isEmpty())
                .count());
        stats.put("professorsWithProjects", projectRepository.findAll().stream()
                .map(Project::getProfessor)
                .filter(prof -> prof != null)
                .distinct()
                .count());

        return stats;
    }
}