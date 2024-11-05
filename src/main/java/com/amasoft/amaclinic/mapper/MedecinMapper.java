package com.amasoft.amaclinic.mapper;

import com.amasoft.amaclinic.Entity.Medecin;
import com.amasoft.amaclinic.dto.request.MedecinRequestDto;
import com.amasoft.amaclinic.dto.response.MedecinResponseDto;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface MedecinMapper {

    @Mapping(source = "specialite.codeSpecialite", target = "specialite")
    MedecinResponseDto modelToDto(Medecin medecin);

    Medecin dtoToModel(MedecinRequestDto medecinRequestDto);

    default Page<MedecinResponseDto> modelToDtos(Page<Medecin> medecinPage) {
        return medecinPage.map(this::modelToDto);
    }
}
