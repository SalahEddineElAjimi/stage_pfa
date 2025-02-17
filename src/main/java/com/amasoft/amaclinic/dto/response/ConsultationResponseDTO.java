package com.amasoft.amaclinic.dto.response;

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
public class ConsultationResponseDTO {
    private String codeConsultation;
    private LocalDateTime date;
    private RendezVousResponseDTO rendezVousResponseDto;
    private PaimentResponseDto paiment;
    private String codeSalle;


}
