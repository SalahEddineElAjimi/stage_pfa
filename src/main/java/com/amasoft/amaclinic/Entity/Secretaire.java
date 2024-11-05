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
@Table(name = "secretaire")
public class Secretaire {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id ;

    @Column(name="codeSecretaire",unique = true)
    private String codeSecretaire;
    @Column(name="nom")
    private String nom ;
    @Column(name="prenom")
    private String prenom ;
    @Column(name="cin")
    private String cin;
    @Column(name="adresse")
    private String adresse ;
    @Column(name="telephone",length = 10)
    private String telephone ;
}
