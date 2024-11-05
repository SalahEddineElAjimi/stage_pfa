package com.amasoft.amaclinic.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrdonnanceResponseDTO {

    private String codeOrdonnance;
    private String detailsOrdonnance;
    private LocalDate dateOrdonnance;
    private ConsultationResponseDTO consultation;
    private List<OrdonnanceDetailsResponseDTO> ordonnanceDetails;



}



