package com.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackResponse {
    private Long id;
    private String commentaire;
    private Integer note;
    private LocalDate date;
    private Long phaseId; // Changed from projectId
    private Long professorId;
    private String professorName;
}