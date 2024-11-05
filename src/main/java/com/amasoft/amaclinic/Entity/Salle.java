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
public class Salle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idSalle;

    @Column(name = "codeSalle", unique = true, nullable = false)
    private String codeSalle;

    @Column(name = "nomSalle")
    private String nomSalle;

    @Column(name = "type")
    private String type;

    @ManyToOne
    @JoinColumn(name = "cabinet_id", nullable = false)
    private Cabinet cabinet;


    @ManyToMany(mappedBy = "salles")
    private Set<Equipement> equipements;

    @OneToOne(mappedBy = "salle", cascade = CascadeType.ALL)
    private Consultation consultation;


}
