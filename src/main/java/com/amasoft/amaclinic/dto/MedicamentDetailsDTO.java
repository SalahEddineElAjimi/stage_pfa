package com.amasoft.amaclinic.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MedicamentDetailsDTO {
    private String nomMedicament;
    private String typeMedicament;
    private String dose;
    private Integer dureeTraitement;
    private Integer frequence;
    private String horaires;


}
