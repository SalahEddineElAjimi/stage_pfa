package com.amasoft.amaclinic.criteria;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TypeAssuranceCriteria {
    private Long id;
    private String codeAssuranceType;
    private String nomAssurance;
    private String typeAssurance;
}
