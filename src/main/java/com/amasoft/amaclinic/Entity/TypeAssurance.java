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
public class TypeAssurance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "codeAssuranceType",unique = true)
    private String codeAssuranceType;

    @Column(name = "nomAssurance")
    private String nomAssurance; // CNSS,AMO,CNOPS,SAHAM,Allianz..

    @Column(name = "typeAssurance")
    private String typeAssurance; //Publique,Privées,Mutuelles,Complémentaires..

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "typeAssurance")
    private Set<AssurancePatient> assurancePatients;

}
