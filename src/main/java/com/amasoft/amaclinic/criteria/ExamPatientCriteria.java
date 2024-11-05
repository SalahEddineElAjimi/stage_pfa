package com.amasoft.amaclinic.criteria;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExamPatientCriteria {
    private Long id ;
    private String codeExamPatient ;
    private LocalDate dateExam ;
    private String detail ;
    private String type ;
    private String codePatient ;
}
