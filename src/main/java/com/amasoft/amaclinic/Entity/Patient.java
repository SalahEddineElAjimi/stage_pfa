package com.amasoft.amaclinic.Entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "codePatient", unique = true)
    private String codePatient;
    @Column(name = "prenom")
    private String prenom;
    @Column(name = "nom")
    private String nom;
    @Column(name="age")
    private String age;
    @Column(name = "cin")
    private String cin;
    @Column(name = "sexe")
    private String sexe;
    @Column(name = "telephone", length = 10)
    private String telephone;
    @Column(name = "email")
    private String email;
    @Column(name = "address")
    private String address;

    @Column(name = "ville")
    private String ville;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL)
    private List<ParametresVitaux> parametresVitauxList = new ArrayList<>();

    @OneToOne(mappedBy = "patient", cascade = CascadeType.ALL)
    private AssurancePatient assurancePatient;

    @OneToMany(mappedBy = "patient")
    private Set<RendezVous> rendezVous;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL)
    private List<ExamPatient> ExamPatient = new ArrayList<>();

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL)
    private List<Soignement> soignements = new ArrayList<>();
}
