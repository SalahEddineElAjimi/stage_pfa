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
public class Soignement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;
    @Column(name = "codeSoignement" , unique = true)
    private String codeSoignement ;
    @Column(name = "typeSoignement")
    private String typeSoignement ;
    @Column(name = "description")
    private String description ;
    @Transient
    private String patientCode ;
    @ManyToOne
    @JoinColumn(name = "codePatient")
    private Patient patient;
    @Transient
    private String infermiereCode ;
    @ManyToOne
    @JoinColumn(name = "codeInfermiere")
    private Infermiere infermiere;
}
