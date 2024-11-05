package com.amasoft.amaclinic.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SoignementResponseDTO {
    private String codeSoignement ;
    private String typeSoignement ;
    private String description ;
    private PatientResponseDto patient ;
    private InfermiereResponseDTO infermiere ;


}
