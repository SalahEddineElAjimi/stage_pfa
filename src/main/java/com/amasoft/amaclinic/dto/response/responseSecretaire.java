package com.amasoft.amaclinic.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class responseSecretaire {
    private String codeSecretaire ;
    private String nom ;
    private String prenom ;
    private String adresse ;
    private String telephone ;

}
