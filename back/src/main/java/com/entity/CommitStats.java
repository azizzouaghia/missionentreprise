package com.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "commit_stats")
public class CommitStats {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "INT")
    private Long id;

    private Integer nbCommits;
    private Integer nbMergeRequests;

    @OneToOne(mappedBy = "commitStats")
    private StatistiquesDTO statistiques;
}