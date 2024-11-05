package com.amasoft.amaclinic.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AbsenceInfermiereResponseDTO {
    private String codeAbscence;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private Long idInfermiere;
}
