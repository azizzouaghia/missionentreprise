package com.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StatsResponse {
    private Integer totalProjects;
    private Integer activeProjects;
    private Integer totalStudents;
    private Integer totalProfessors;
    private Double averageFeedbackScore;
}