package com.amasoft.amaclinic.Entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ParametresVitaux {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "codeParametre",unique = true)
    private String codeParametre;
    @Column(name = "poids")
    private String poids;
    @Column(name = "hauteur")
    private String hauteur;

    @Column(name = "temperatureCorporelle",length = 10)
    private String temperatureCorporelle;
    @Column(name = "rythmCardiaque")
    private String rythmCardiaque;
    @Column(name = "frequenceRespiratoire")
    private String frequenceRespiratoire;
    @Column(name = "pressionArterielle")
    private String pressionArterielle;
    @Transient
    private String patientCode;
    @ManyToOne
    @JoinColumn(name="patient_id")
    private Patient patient;
}
