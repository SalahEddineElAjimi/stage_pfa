package com.amasoft.amaclinic.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MedecinResponseDto {
    private String codeMedecin;
    private String prenom;
    private String nom;
    private String cin;
    private String telephone;
    private String email;
    private String address;
    private String ville;
    private String specialite;
}
