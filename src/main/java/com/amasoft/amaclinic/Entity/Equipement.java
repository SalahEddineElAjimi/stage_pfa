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
public class Equipement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idEquipement;

    @Column(name = "codeEquipement", unique = true)
    private String codeEquipement;

    @Column(name = "nomEquipement")
    private String nomEquipement;

    @Column(name = "typeEquipement")
    private String typeEquipement;


    @ManyToMany
    @JoinTable(
            name = "equipement_salle",
            joinColumns = @JoinColumn(name = "id_equipement"),
            inverseJoinColumns = @JoinColumn(name = "id_salle"))
    private Set<Salle> salles;

}
