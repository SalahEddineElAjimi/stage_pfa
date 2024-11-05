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
public class CentreRequestDTO {
    private String codeCentre;
    private String nomCentre;
    private String adresse;
    private String typeCentre;
    private String telephone;

}
