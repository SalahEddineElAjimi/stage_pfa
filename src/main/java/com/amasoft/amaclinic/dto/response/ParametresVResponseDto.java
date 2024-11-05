package com.amasoft.amaclinic.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ParametresVResponseDto {
    private String codeParametre;
    private String poids;
    private String hauteur;
    private String temperatureCorporelle;
    private String rythmCardiaque;
    private String frequenceRespiratoire;
    private String pressionArterielle;
    private PatientResponseDto patient;

}
