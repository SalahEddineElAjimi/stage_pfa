package com.amasoft.amaclinic.Entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RendezVous {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "codeRendezVous",unique = true)
    private String codeRendezVous;

    @Column(name = "status")
    private String status;  //accepte , non accepte..
    @Column(name = "typeRDV")
    private String typeRDV;  //RDV ou Controle

    @Column(name = "dateDebutRDV")
    private LocalDateTime dateDebutRDV;
    @Column(name = "dateFinRDV")
    private LocalDateTime dateFinRDV;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "medecin_id")
    private Medecin medecin;

    @OneToOne(mappedBy = "rendezVous")
    private Consultation consultation;


}
