package com.esprit.tn.aziz_zouaghia_tpfoyer.entity;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Data
public class Feedback {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String commentaire;
    private Integer note;
    private LocalDate date;
    
    @ManyToOne
    private Project project;
    
    @ManyToOne
    private Professor professor;
}