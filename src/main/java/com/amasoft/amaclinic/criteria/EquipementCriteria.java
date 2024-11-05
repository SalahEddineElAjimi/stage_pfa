package com.amasoft.amaclinic.criteria;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EquipementCriteria {

    private String codeEquipement;
    private String nomEquipement;
    private Long salleId;

}
