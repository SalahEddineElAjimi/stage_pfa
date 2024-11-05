package com.amasoft.amaclinic.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResultatConsltResponseDTO {
    private String codeResultatConsultation ;
    private String motif ;
    private String description;
    private ConsultationResponseDTO consultation ;
}
