package com.amasoft.amaclinic.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaimentRequestDto {

    private String codePaiment;
    private LocalDateTime datePaiment;
    private int montant;
    private String typePaiment;
    private String codeConsultation;

}
