package com.amasoft.amaclinic.dto.request;


import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class InfermiereRequestDTO {

    private String codeInferm;
    private String nom;
    private String prenom;
    private String email;
    private String ville;
    private String cin;
    private String adresse;
    private String telephone;
    private String codeSpecialite;

}
