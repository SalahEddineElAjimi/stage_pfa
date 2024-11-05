package com.amasoft.amaclinic.criteria;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParametresVCriteria {
    private Long id;
    private String parametreCode;
    private String poids;
    private String hauteur;
    private String temperatureCorporelle;
    private String rythmCardiaque;
    private String frequenceRespiratoire;
    private String pressionArterielle;
    private String patientCode;

}
