package com.amasoft.amaclinic.Service;

import com.amasoft.amaclinic.criteria.AssurancePatientCriteria;

import com.amasoft.amaclinic.dto.request.AssurancePatientRequestDto;
import com.amasoft.amaclinic.dto.response.AssurancePatientResponseDto;
import org.springframework.data.domain.Page;

import java.io.IOException;

public interface AssurancePatientService {

    AssurancePatientResponseDto addAssurancePatient(AssurancePatientRequestDto assurancePatientRequestDto);
    AssurancePatientResponseDto updateAssurancePatient(AssurancePatientRequestDto assurancePatientRequestDto);
    void  deleteAssurancePatient(String codeParametre);

    Page<AssurancePatientResponseDto> findAssurancePatientByCriteria(AssurancePatientCriteria assurancePatientCriteria, int page , int size);
    public byte[] generateExcel() throws IOException;


}
