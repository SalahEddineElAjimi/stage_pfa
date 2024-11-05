package com.amasoft.amaclinic.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Centre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codeCentre", unique = true)
    private String codeCentre;

    @Column(name = "nomCentre")
    private String nomCentre;

    @Column(name = "adresse")
    private String adresse;

    @Column(name = "typeCentre")
    private String typeCentre;

    @Column(name = "telephone")
    private String telephone;

    @ManyToMany(mappedBy = "centres")
    private List<Cabinet> cabinets;

}
