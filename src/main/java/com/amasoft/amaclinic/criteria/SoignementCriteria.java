package com.amasoft.amaclinic.criteria;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SoignementCriteria {
    private Long id ;
    private String codeSoignement ;
    private String typeSoignement ;
    private String description ;
    private String codePatient ;
    private String codeInfermiere ;
}
