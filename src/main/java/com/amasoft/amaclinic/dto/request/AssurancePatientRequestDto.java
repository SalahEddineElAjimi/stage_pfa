package com.amasoft.amaclinic.dto.request;

import com.amasoft.amaclinic.Entity.Patient;
import com.amasoft.amaclinic.Entity.TypeAssurance;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AssurancePatientRequestDto {

    private String codeAssurance;
    private String numeroMatricule;
    private LocalDate dateDebutCouverture;
    private LocalDate dateFinCouverture;
    private String detailsCouverture;
    private String statut;
    private String codePatient;
    private String codeTypeAssurance;

}
