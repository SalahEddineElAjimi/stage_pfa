package com.amasoft.amaclinic.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Ordonnance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codeOrdonnance", unique = true)
    private String codeOrdonnance;

    @Column(name = "detailsOrdonnance")
    private String detailsOrdonnance;

    @Column(name = "dateOrdonnance")
    private LocalDate dateOrdonnance;

    @OneToOne
    @JoinColumn(name = "consultation_id")
    private Consultation consultation;


    @OneToMany(mappedBy = "ordonnance")
    private List<OrdonnanceDetails> ordonnanceDetails;
}



