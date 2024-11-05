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
public class ExamPatientRequestDto {
        private Long id ;
        private String codeExamPatient;
        private LocalDate dateExam ;
        private String detail ;
        private String type ;
        private String codePatient;
}
