package com.amasoft.amaclinic.mapper;

import com.amasoft.amaclinic.Entity.Paiment;
import com.amasoft.amaclinic.dto.request.PaimentRequestDto;
import com.amasoft.amaclinic.dto.response.PaimentResponseDto;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface PaimentMapper {

    @Mapping(source = "codeConsultation", target = "consultation.codeConsultation")
    Paiment toEntity(PaimentRequestDto paimentRequestDto);

    @Mapping(source = "consultation.codeConsultation", target = "codeConsultation")
    PaimentResponseDto toResponseDto(Paiment paiment);

    default Page<PaimentResponseDto> toResponseDtoPage(Page<Paiment> paimentPage) {
        return paimentPage.map(this::toResponseDto);
    }

    Paiment dtoToModel(PaimentRequestDto paimentRequestDto);

    PaimentRequestDto modelToDto(Paiment paiment);
}
