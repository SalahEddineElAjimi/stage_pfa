package com.amasoft.amaclinic.criteria;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientCriteria {
    private Long id;
    private String patientCode;
    private String prenom;
    private String nom;

    private String cin;

}
