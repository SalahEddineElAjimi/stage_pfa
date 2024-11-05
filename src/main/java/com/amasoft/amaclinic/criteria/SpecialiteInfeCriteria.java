package com.amasoft.amaclinic.criteria;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SpecialiteInfeCriteria {

    private Long idSpecialite;
    private String codeSpecialite;
    private String nomSpecialite;
    private String infermiereCode;
}
