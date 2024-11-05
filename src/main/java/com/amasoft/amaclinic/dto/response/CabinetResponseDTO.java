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
public class CabinetResponseDTO {

    private Long idCabinet;
    private String codeCabinet;
    private String nomCabinet;

    private String email;


    private List<CentreResponseDTO> centres;

    public String getNom_cabinet() {
        return nomCabinet;
    }

    public void setNom_cabinet(String nom_cabinet) {
        this.nomCabinet = nom_cabinet;
    }
}
