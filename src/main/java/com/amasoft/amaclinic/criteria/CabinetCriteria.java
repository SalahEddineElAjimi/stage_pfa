package com.amasoft.amaclinic.criteria;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CabinetCriteria {

    private Long id;
    private String codeCabinet;
    private String nomCabinet;
    private String adresse;

    public String getNom_cabinet() {
        return nomCabinet;
    }


    public void setNom_cabinet(String nomCabinet) {
        this.nomCabinet = nomCabinet;
    }

}
