package com.amasoft.amaclinic.mapper;



import com.amasoft.amaclinic.Entity.AssurancePatient;
import com.amasoft.amaclinic.dto.request.AssurancePatientRequestDto;
import com.amasoft.amaclinic.dto.response.AssurancePatientResponseDto;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))

public interface AssurancePatientMapper {
    AssurancePatient dtoToModel(AssurancePatientRequestDto assurancePatientRequestDto);

    AssurancePatientResponseDto modelToDto(AssurancePatient assurancePatient);

    default Page<AssurancePatientResponseDto> modelToDtos(Page<AssurancePatient> assurancePatientPage) {
        return assurancePatientPage.map(this::modelToDto);
    }

}
