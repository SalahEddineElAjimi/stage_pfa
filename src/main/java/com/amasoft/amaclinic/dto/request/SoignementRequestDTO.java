package com.amasoft.amaclinic.dto.request;

import com.amasoft.amaclinic.Entity.Patient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SoignementRequestDTO {
    private Long id;
    private String codeSoignement ;
    private String typeSoignement ;
    private String description ;
    private String codePatient ;
    private String codeInfermiere ;
}
