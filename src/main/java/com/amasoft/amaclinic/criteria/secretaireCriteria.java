package com.amasoft.amaclinic.criteria;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class secretaireCriteria {
    private Long id ;
    private String codeSecretaire;
    private String nom ;
    private String prenom ;
}
