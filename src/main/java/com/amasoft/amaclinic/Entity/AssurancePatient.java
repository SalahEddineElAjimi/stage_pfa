package com.amasoft.amaclinic.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AssurancePatient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "codeAssurance",unique = true)
    private String codeAssurance;

    @Column(name = "numeroMatricule", unique = true)
    private String numeroMatricule;

    @Column(name = "dateDebutCouverture")
    private LocalDate dateDebutCouverture;

    @Column(name = "dateFinCouverture")
    private LocalDate dateFinCouverture;

    @Column(name = "detailsCouverture")
    private String detailsCouverture; //types de services médicaux, médicaments...

    @Column(name = "statut")
    private String statut;//actif, expiree, en cours de renouvellement

    @Transient
    private String codePatient;

    @OneToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "typeAssurance_id")
    private TypeAssurance typeAssurance;



}
