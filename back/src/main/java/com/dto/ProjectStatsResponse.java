package com.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProjectStatsResponse {
    private Long projectId;
    private Integer commitCount;
    private Double averageNote;
    private Integer feedbackCount;
    private Integer teamSize;
}