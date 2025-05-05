package com.esprit.tn.aziz_zouaghia_tpfoyer.entity;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
public class Project {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String titre;
    private String description;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    
    @ManyToMany
    @JoinTable(
        name = "etudiant_projet",
        joinColumns = @JoinColumn(name = "projet_id"),
        inverseJoinColumns = @JoinColumn(name = "etudiant_id"))
    private Set<Student> students = new HashSet<>();
    
    @OneToMany(mappedBy = "project")
    private Set<Feedback> feedbacks = new HashSet<>();
    
    private String gitlabProjectId;
}