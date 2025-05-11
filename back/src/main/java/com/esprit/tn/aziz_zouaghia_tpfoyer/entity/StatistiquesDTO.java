package com.esprit.tn.aziz_zouaghia_tpfoyer.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "statistiquesdto")
public class StatistiquesDTO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "INT")
    private Long id;

    @OneToOne(optional = true)
    @JoinColumn(name = "commit_stats_id")
    private CommitStats commitStats;

    @OneToOne(optional = true)
    @JoinColumn(name = "projet_stats_id")
    private ProjectStats projectStats;
}