package com.amasoft.amaclinic.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrdonnanceRequestDTO {

    private Long id;

    private String codeOrdonnance;

    private String detailsOrdonnance;

    private LocalDate dateOrdonnance;

    private String codeConsultation;




}



