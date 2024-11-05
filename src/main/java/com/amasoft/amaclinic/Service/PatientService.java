package com.amasoft.amaclinic.Service;

import com.amasoft.amaclinic.criteria.PatientCriteria;
import com.amasoft.amaclinic.dto.request.PatientRequestDto;
import com.amasoft.amaclinic.dto.response.PatientResponseDto;
import org.springframework.data.domain.Page;

import java.io.IOException;

public interface PatientService {
    PatientResponseDto addPatient(PatientRequestDto patientRequestDto);
    PatientResponseDto updatePatient(PatientRequestDto patientRequestDto);
    void  deletePatient(String codePatient);

    Page<PatientResponseDto> findPatientByCriteria(PatientCriteria patientCriteria, int page , int size);
    byte[] generatePatientExcel() throws IOException;







}
