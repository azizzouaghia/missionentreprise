package com.service;

import com.entity.Role;
import com.repository.ProjectRepository;
import com.repository.UserRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Service
public class StatsService {
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    public StatsService(ProjectRepository projectRepository, 
                      UserRepository userRepository) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    public Map<String, Object> getStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("nbProfs", userRepository.countByRole(Role.PROF));
        stats.put("nbEtudiants", userRepository.countByRole(Role.ETUDIANT));
        return stats;
    }
}