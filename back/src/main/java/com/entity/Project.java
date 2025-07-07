package com.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Data
// --- REMOVED "feedbacks" FROM THE EXCLUDE LIST ---
@EqualsAndHashCode(exclude = {"students", "professor", "phases"})
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

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
        name = "etudiant_projet",
        joinColumns = @JoinColumn(name = "project_id"),
        inverseJoinColumns = @JoinColumn(name = "etudiant_id"))
    @JsonIgnoreProperties("projects")
    private Set<Student> students = new HashSet<>();

    // --- THIS ENTIRE BLOCK WAS REMOVED AS IT'S NO LONGER VALID ---
    // @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    // @JsonIgnoreProperties("project")
    // private Set<Feedback> feedbacks = new HashSet<>();

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("project")
    private Set<Phase> phases = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "professor_id")
    @JsonIgnoreProperties("givenFeedbacks")
    private Professor professor;

    private String gitlabProjectId;

    public void addStudent(Student student) {
        this.students.add(student);
        student.getProjects().add(this);
    }

    public void removeStudent(Student student) {
        this.students.remove(student);
        student.getProjects().remove(this);
    }
}