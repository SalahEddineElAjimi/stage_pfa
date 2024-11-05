package com.amasoft.amaclinic.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InfermiereResponseDTO {

    private String codeInferm;
    private String nom;
    private String prenom;
    private String email;
    private String ville;
    private String cin;
    private String adresse;
    private String telephone;
}
