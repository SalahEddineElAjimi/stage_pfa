package com.amasoft.amaclinic.mapper;

import com.amasoft.amaclinic.Entity.Patient;
import com.amasoft.amaclinic.dto.request.PatientRequestDto;
import com.amasoft.amaclinic.dto.response.PatientResponseDto;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface PatientMapper {

    Patient dtoToModel(PatientRequestDto patientRequestDto);
    PatientResponseDto modelToDto(Patient patient);

    default Page<PatientResponseDto> modelToDtos(Page<Patient> patientPage) {
        return patientPage.map(this::modelToDto);
    }
}
