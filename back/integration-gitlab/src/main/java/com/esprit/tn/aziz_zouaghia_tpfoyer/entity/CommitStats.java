package com.esprit.tn.aziz_zouaghia_tpfoyer.entity;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class CommitStats {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private Integer nbCommits;
    private Integer nbMergeRequests;
    
    @OneToOne(mappedBy = "commitStats")
    private StatistiquesDTO statistiques;
}