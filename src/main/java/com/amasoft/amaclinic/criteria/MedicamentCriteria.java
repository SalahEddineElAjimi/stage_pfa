package com.amasoft.amaclinic.criteria;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MedicamentCriteria {
    private Long id;
    private String codeMedicament;
    private String nomMedicament;
    private String typeMedicament;
}
