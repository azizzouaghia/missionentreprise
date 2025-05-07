package com.esprit.tn.aziz_zouaghia_tpfoyer.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Table(name = "project")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", columnDefinition = "INT")
    private Long id;

    private String titre;
    private String description;
    private LocalDate dateDebut;
    private LocalDate dateFin;

    @ManyToMany
    @JoinTable(
        name = "etudiant_projet",
        joinColumns = @JoinColumn(name = "project_id"),
        inverseJoinColumns = @JoinColumn(name = "etudiant_id"))
    private Set<Student> students = new HashSet<>();

    @OneToMany(mappedBy = "project")
    private Set<Feedback> feedbacks = new HashSet<>();

    private String gitlabProjectId;
}