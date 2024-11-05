package com.amasoft.amaclinic.Entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Medicament {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codeMedicament", unique = true)
    private String codeMedicament;

    @Column(name = "nomMedicament")
    private String nomMedicament;

    @Column(name = "typeMedicament")
    private String typeMedicament;

    @OneToMany(mappedBy = "medicament")
    private List<OrdonnanceDetails> ordonnanceDetails;


}
