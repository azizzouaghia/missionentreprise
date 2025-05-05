package com.esprit.tn.aziz_zouaghia_tpfoyer.entity;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class ProjectStats {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private Integer nbProjets;
    private Integer projetsActifs;
    
    @OneToOne(mappedBy = "projectStats")
    private StatistiquesDTO statistiques;
}