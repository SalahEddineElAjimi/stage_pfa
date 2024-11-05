package com.amasoft.amaclinic.criteria;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InfermiereCriteria {
    private Long id;
    private String nom;
    private String prenom;
    private String email;
    private String ville;
    private String cin;
    private String adresse;
    private String telephone;


}

