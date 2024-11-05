package com.amasoft.amaclinic.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Infermiere {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idInfermiere;

    @Column(name = "codeInferm", unique = true)
    private String codeInferm;

    private String nom;

    private String prenom;

    private String email;

    private String ville;

    @Column(name = "cin", unique = true)
    private String cin;

    private String adresse;

    @Column(name = "telephone", length = 10)
    private String telephone;

    @ManyToOne
    private SpecialiteInfermiere specialite;

    @OneToMany(mappedBy = "infermiere", cascade = CascadeType.ALL)
    private List<AbsenceInfermiere> absences;

    @OneToMany(mappedBy = "infermiere", cascade = CascadeType.ALL)
    private List<Soignement> soignements = new ArrayList<>();
}
