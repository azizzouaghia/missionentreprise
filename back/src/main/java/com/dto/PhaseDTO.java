package com.dto;

import com.entity.Phase;
import com.entity.PhaseStatus;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PhaseDTO {
    private Long id;
    private String name;
    private String description;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private PhaseStatus status;
    private Long projectId;
    
    // FIX: Ensure this field exists
    private String commitId;

    public static PhaseDTO fromEntity(Phase phase) {
        PhaseDTO dto = new PhaseDTO();
        dto.setId(phase.getId());
        dto.setName(phase.getName());
        dto.setDescription(phase.getDescription());
        dto.setDateDebut(phase.getDateDebut());
        dto.setDateFin(phase.getDateFin());
        dto.setStatus(phase.getStatus());
        dto.setProjectId(phase.getProject().getId());
        
        // FIX: Ensure this mapping line exists
        dto.setCommitId(phase.getCommitId());
        
        return dto;
    }

    public Phase toEntity() {
        Phase phase = new Phase();
        phase.setId(this.id);
        phase.setName(this.name);
        phase.setDescription(this.description);
        phase.setDateDebut(this.dateDebut);
        phase.setDateFin(this.dateFin);
        phase.setStatus(this.status);
        
        // FIX: Ensure this mapping line exists
        phase.setCommitId(this.commitId);
        
        return phase;
    }
}