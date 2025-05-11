package com.esprit.tn.aziz_zouaghia_tpfoyer.dto;

import lombok.Data;

@Data
public class FeedbackRequest {
    private String commentaire;
    private Integer note;
    private Long projectId;
    private Long professorId;
}