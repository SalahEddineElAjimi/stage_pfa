package com.amasoft.amaclinic.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Medecin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "codeMedecin",unique = true)
    private String codeMedecin;
    @Column(name = "prenom")
    private String prenom;
    @Column(name = "nom")
    private String nom;
    @Column(name = "cin")
    private String cin;
    @Column(name = "telephone",length = 10)
    private String telephone;
    @Column(name = "email")
    private String email;
    @Column(name = "address")
    private String address;
    @Column(name = "ville")
    private String ville;

    @OneToMany(mappedBy = "medecin")
    private Set<RendezVous> rendezVous;

    @ManyToOne

    private MedecinSpecialites specialite;

}
