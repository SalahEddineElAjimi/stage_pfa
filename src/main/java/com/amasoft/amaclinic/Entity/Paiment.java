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
public class Paiment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPaiment;

    @Column(name = "codePaiment", unique = true, nullable = false)
    private String codePaiment;

    @Column(name = "datePaiment", nullable = false)
    private LocalDateTime datePaiment;

    @Column(name = "montant", nullable = false)
    private int montant;

    @Column(name = "typePaiment", nullable = false)
    private String typePaiment;

    @OneToOne
    @JoinColumn(name = "consultation_id", nullable = false)
    private Consultation consultation;
}
