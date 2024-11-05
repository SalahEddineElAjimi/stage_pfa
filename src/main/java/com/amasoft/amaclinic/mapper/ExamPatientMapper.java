package com.amasoft.amaclinic.mapper;

import com.amasoft.amaclinic.Entity.ExamPatient;
import com.amasoft.amaclinic.dto.request.ExamPatientRequestDto;
import com.amasoft.amaclinic.dto.response.ExamPatientResponseDto;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface ExamPatientMapper {
    ExamPatient dtoToModel(ExamPatientRequestDto examPatientRequestDto);
    @Mapping(source = "patient" , target = "patient")
    ExamPatientResponseDto modelToDto(ExamPatient examPatient);

    default Page<ExamPatientResponseDto> modelToDtos(Page<ExamPatient> patientPage)
    {
     return patientPage.map(this::modelToDto);
    }


}
