package com.amasoft.amaclinic.Service;

import com.amasoft.amaclinic.criteria.ExamPatientCriteria;
import com.amasoft.amaclinic.dto.request.ExamPatientRequestDto;
import com.amasoft.amaclinic.dto.response.ExamPatientResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.io.IOException;

public interface ExamPatientService {
        ExamPatientResponseDto addExamPatient(ExamPatientRequestDto examPatientRequestDto);
        ExamPatientResponseDto updateExamPatient(ExamPatientRequestDto examPatientRequestDto);
        void deleteExamPatient(String codeExamPatient);
        Page<ExamPatientResponseDto> findExamPatientByCriteria(ExamPatientCriteria examPatientCriteria , int page , int size);
        byte[] generatePatientExam() throws IOException;
}
