package com.amasoft.amaclinic.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Consultation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "codeConsultation",unique = true)
    private String codeConsultation;

    @Column(name = "date")
    private LocalDateTime date;

    @OneToOne
    @JoinColumn(name = "rendezVous_id")
    private RendezVous rendezVous;
    @OneToOne(mappedBy = "consultation", cascade = CascadeType.ALL)
    private Paiment paiment;

    @OneToMany(mappedBy = "consultation")
    private Set<Certification> certifications;
    @OneToOne(mappedBy = "consultation" , cascade = CascadeType.ALL)
    private ResultatConsultation resultatConsultation;
    @OneToOne(mappedBy = "consultation", cascade = CascadeType.ALL)
    private Ordonnance ordonnance;

    @OneToOne
    @JoinColumn(name = "salle_id")
    private Salle salle;

}
