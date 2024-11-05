package com.amasoft.amaclinic.criteria;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResultatConsltCriteria {
    private Long id ;
    private String resultaConsultation ;
    private String motif ;
    private String description ;
    private String codeConsultation ;
}
