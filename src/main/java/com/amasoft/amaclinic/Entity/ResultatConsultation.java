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
public class ResultatConsultation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;
    @Column(name = "codeResultatConsultation" , unique = true)
    private String codeResultatConsultation ;
    @Column(name = "motif")
    private String motif ;
    @Column(name = "description")
    private String description;
    @Transient
    private String codeConsultation ;
    @OneToOne
    @JoinColumn(name = "codeConsultation")
    private Consultation consultation;
}
