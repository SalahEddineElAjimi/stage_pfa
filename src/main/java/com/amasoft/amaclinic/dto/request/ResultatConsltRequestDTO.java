package com.amasoft.amaclinic.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResultatConsltRequestDTO {
    private Long id ;
    private String codeResultatConsultation ;
    private String motif;
    private String description ;
    private String codeConsultation ;
}
