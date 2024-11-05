package com.amasoft.amaclinic.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AbsenceInfermiereRequestDTO {

    private Long idAbsence;
    private String codeAbscence;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private String codeInferm;
}
