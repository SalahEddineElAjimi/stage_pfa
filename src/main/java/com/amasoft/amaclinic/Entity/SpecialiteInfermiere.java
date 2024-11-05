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
public class SpecialiteInfermiere {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_specialite")
    private Long id;

    @Column(name = "codeSpecialite", unique = true)
    private String codeSpecialite;

    @Column(name = "nomSpecialite")
    private String nomSpecialite;

    @OneToMany(mappedBy = "specialite")
    private List<Infermiere> infirmieres;
}
