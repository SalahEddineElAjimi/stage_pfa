package com.amasoft.amaclinic.mapper;

import com.amasoft.amaclinic.Entity.SpecialiteInfermiere;
import com.amasoft.amaclinic.dto.request.SpecialiteInfeRequestDto;
import com.amasoft.amaclinic.dto.response.SpecialiteInfeResponseDto;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface SpecialiteInfermiereMapper {

    SpecialiteInfermiere dtoToModel(SpecialiteInfeRequestDto specialiteInfeRequestDto);

    @Mapping(source="codeSpecialite", target = "codeSpecialite")
    SpecialiteInfeResponseDto modelToDto(SpecialiteInfermiere entity);

    default Page<SpecialiteInfeResponseDto> modelToDtos(Page<SpecialiteInfermiere> entityPage) {
        return entityPage.map(this::modelToDto);
    }
}
