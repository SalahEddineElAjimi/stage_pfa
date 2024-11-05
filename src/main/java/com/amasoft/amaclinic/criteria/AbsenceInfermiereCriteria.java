package com.amasoft.amaclinic.criteria;

import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AbsenceInfermiereCriteria {

    private Long idAbsence;
    private String codeAbscence;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private Long idInfermiere;
}
