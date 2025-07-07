package com.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Entity
@Data
@EqualsAndHashCode(exclude = {"phase", "professor"}) // Updated exclude
@Table(name = "feedback")
public class Feedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String commentaire;
    private Integer note;
    private LocalDate date;

    // --- CHANGED RELATIONSHIP FROM PROJECT TO PHASE ---
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "phase_id") // Changed from project_id
    @JsonIgnore
    private Phase phase;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "professor_id")
    @JsonIgnore
    private User professor;
}