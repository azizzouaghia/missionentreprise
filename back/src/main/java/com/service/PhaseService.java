package com.service;

import com.dto.PhaseDTO;
import com.entity.Phase;
import com.entity.Project;
import com.repository.PhaseRepository;
import com.repository.ProjectRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PhaseService {
    private final PhaseRepository phaseRepository;
    private final ProjectRepository projectRepository;

    public PhaseService(PhaseRepository phaseRepository, ProjectRepository projectRepository) {
        this.phaseRepository = phaseRepository;
        this.projectRepository = projectRepository;
    }

    @Transactional
    public PhaseDTO createPhase(PhaseDTO phaseDTO) {
        Project project = projectRepository.findById(phaseDTO.getProjectId())
                .orElseThrow(() -> new RuntimeException("Project not found with id: " + phaseDTO.getProjectId()));

        Phase phase = phaseDTO.toEntity();
        phase.setProject(project);
        Phase savedPhase = phaseRepository.save(phase);
        return PhaseDTO.fromEntity(savedPhase);
    }

    public List<PhaseDTO> getAllPhases() {
        return phaseRepository.findAll().stream()
                .map(PhaseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public PhaseDTO getPhaseById(Long id) {
        Phase phase = phaseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Phase not found with id: " + id));
        return PhaseDTO.fromEntity(phase);
    }

    public List<PhaseDTO> getPhasesByProjectId(Long projectId) {
        return phaseRepository.findByProjectId(projectId).stream()
                .map(PhaseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public PhaseDTO updatePhase(Long id, PhaseDTO phaseDTO) {
        Phase existingPhase = phaseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Phase not found with id: " + id));

        Project project = projectRepository.findById(phaseDTO.getProjectId())
                .orElseThrow(() -> new RuntimeException("Project not found with id: " + phaseDTO.getProjectId()));

        existingPhase.setName(phaseDTO.getName());
        existingPhase.setDescription(phaseDTO.getDescription());
        existingPhase.setDateDebut(phaseDTO.getDateDebut());
        existingPhase.setDateFin(phaseDTO.getDateFin());
        existingPhase.setStatus(phaseDTO.getStatus());
        existingPhase.setProject(project);

        Phase updatedPhase = phaseRepository.save(existingPhase);
        return PhaseDTO.fromEntity(updatedPhase);
    }

    @Transactional
    public void deletePhase(Long id) {
        Phase phase = phaseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Phase not found with id: " + id));
        phaseRepository.delete(phase);
    }
    
    // --- ADDED METHOD ---
    @Transactional
    public PhaseDTO linkCommitToPhase(Long phaseId, String commitId) {
        Phase phase = phaseRepository.findById(phaseId)
                .orElseThrow(() -> new RuntimeException("Phase not found with id: " + phaseId));
        
        phase.setCommitId(commitId);
        
        Phase updatedPhase = phaseRepository.save(phase);
        return PhaseDTO.fromEntity(updatedPhase);
    }
}   