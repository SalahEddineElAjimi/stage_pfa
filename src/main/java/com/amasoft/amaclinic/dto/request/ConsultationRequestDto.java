package com.amasoft.amaclinic.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ConsultationRequestDto {

    private String codeConsultation;
    private LocalDateTime date;
    private String codeRendezVous;
//    private String codePaiement;
    private String codeSalle;

}
