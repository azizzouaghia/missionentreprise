package com.dto;

import lombok.Data;

@Data
public class FeedbackRequest {
    private String commentaire;
    private Integer note;
    private Long phaseId; // Changed from projectId
    private Long professorId;
}