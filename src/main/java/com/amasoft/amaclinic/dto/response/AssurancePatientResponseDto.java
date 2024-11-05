package com.amasoft.amaclinic.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AssurancePatientResponseDto {

    private String codeAssurance;
    private String numeroMatricule;
    private LocalDate dateDebutCouverture;
    private LocalDate dateFinCouverture;
    private String detailsCouverture;
    private String statut;
    private PatientResponseDto patient;
}
