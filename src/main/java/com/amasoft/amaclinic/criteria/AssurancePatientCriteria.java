package com.amasoft.amaclinic.criteria;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssurancePatientCriteria {
    private Long id;
    private String codeAssurance;
    private String numeroMatricule;
    private LocalDate dateDebutCouverture;
    private LocalDate dateFinCouverture;
    private String statut;
}
