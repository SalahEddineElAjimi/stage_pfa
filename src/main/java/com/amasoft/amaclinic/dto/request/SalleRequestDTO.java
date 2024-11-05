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
public class SalleRequestDTO {
    private Long id;

    private String codeSalle;
    private String nomSalle;
    private String type;
    private String codeCabinet;
    private List<String> codeEquipements;
}
