package com.esprit.tn.aziz_zouaghia_tpfoyer.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Data
@EqualsAndHashCode(callSuper = true, exclude = "projects")
@DiscriminatorValue("ETUDIANT")
public class Student extends User {
    private String matricule;
    private String niveau;

    @ManyToMany(mappedBy = "students", fetch = FetchType.LAZY)
    @JsonIgnoreProperties("students")
    private Set<Project> projects = new HashSet<>();
}