package com.amasoft.amaclinic.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CertificationResponseDTO {
    private Long id;
    private String code;
    private String type;
    private LocalDateTime dateDebut;
    private LocalDateTime dateFin;
    private ConsultationResponseDTO consultationResponseDto;
}
