package com.amasoft.amaclinic.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class secretaireRequestDto {
    private Long id ;
    private String codeSecretaire ;
    private String nom ;
    private String prenom ;
    private String adresse ;
    private String telephone ;
}
