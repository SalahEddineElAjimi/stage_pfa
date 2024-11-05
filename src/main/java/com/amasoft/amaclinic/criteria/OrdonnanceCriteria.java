package com.amasoft.amaclinic.criteria;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrdonnanceCriteria {

    private Long id;

    private String codeOrdonnance;

    private String detailsOrdonnance;

    private LocalDate dateOrdonnance;


}



