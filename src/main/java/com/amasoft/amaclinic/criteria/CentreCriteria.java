package com.amasoft.amaclinic.criteria;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CentreCriteria {
    private Long id;
    private String codeCentre;
    private String nomCentre;
    private String adresse;
    private String typeCentre;
    private String telephone;
}
