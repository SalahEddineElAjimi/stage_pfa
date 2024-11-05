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
public class CabinetRequestDTO {

    private Long id;
    private String codeCabinet;
    private String nomCabinet;
    private String adresse;
    private String email;
    private List<String> codeCentres;

}
