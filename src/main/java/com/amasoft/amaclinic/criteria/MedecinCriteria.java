package com.amasoft.amaclinic.criteria;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MedecinCriteria {
    private Long id;
    private String medecinCode;
    private String prenom;
    private String nom;
    private String cin;

}
