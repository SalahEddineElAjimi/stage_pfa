package com.amasoft.amaclinic.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MedicamentRequestDTO {

    private Long id;
    private String codeMedicament;
    private String nomMedicament;
    private String typeMedicament;

}
