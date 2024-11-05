package com.amasoft.amaclinic.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SalleResponceDTO {


    private String codeSalle;
    private String nomSalle;
    private String type;
    private String codeCabinet;
    private List<EquipementResponseDTO> equipements;
}
