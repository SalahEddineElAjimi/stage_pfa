package com.amasoft.amaclinic.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PatientRequestDto {
    private Long id;
    private String codePatient;
    private String prenom;
    private String nom;
    private String age;

    private String cin;
    private String sexe;
    private String telephone;
    private String email;
    private String address;
    private String ville;

}
