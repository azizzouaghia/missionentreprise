package com.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "project_stats")
public class ProjectStats {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "INT")
    private Long id;

    private Integer nbProjets;
    private Integer projetsActifs;

    @OneToOne(mappedBy = "projectStats")
    private StatistiquesDTO statistiques;
}