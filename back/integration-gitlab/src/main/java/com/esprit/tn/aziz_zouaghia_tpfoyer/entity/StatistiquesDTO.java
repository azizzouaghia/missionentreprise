package com.esprit.tn.aziz_zouaghia_tpfoyer.entity;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class StatistiquesDTO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne
    private CommitStats commitStats;
    
    @OneToOne
    private ProjectStats projectStats;
}