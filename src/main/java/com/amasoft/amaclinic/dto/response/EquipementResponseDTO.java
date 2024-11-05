package com.amasoft.amaclinic.dto.response;

import com.amasoft.amaclinic.Entity.Equipement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EquipementResponseDTO {

    private Long idEquipement;
    private String codeEquipement;
    private String nomEquipement;

}
