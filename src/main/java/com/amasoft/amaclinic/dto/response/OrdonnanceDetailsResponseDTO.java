package com.amasoft.amaclinic.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrdonnanceDetailsResponseDTO {

    private String codeOrdonnanceDetails;
    private String dose;
    private String horaires ;
    private Integer dureeTraitement;
    private String instructionsSpeciales;
    private Integer frequence;

}
