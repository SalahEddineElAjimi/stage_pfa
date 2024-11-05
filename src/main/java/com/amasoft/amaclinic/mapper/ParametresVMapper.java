package com.amasoft.amaclinic.mapper;


import com.amasoft.amaclinic.Entity.ParametresVitaux;
import com.amasoft.amaclinic.dto.request.ParametresVRequestDto;
import com.amasoft.amaclinic.dto.response.ParametresVResponseDto;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface ParametresVMapper {

    ParametresVitaux dtoToModel(ParametresVRequestDto parametresVRequestDto);
    @Mapping(source = "patient", target = "patient")
    ParametresVResponseDto modelToDto(ParametresVitaux parametresVitaux);

    default Page<ParametresVResponseDto> modelToDtos(Page<ParametresVitaux> parametresVitauxPage) {
        return parametresVitauxPage.map(this::modelToDto);
    }
}
