package com.amasoft.amaclinic.mapper;
import com.amasoft.amaclinic.Entity.MedecinSpecialites;
import com.amasoft.amaclinic.dto.request.SpecialiteMedRequestDto;
import com.amasoft.amaclinic.dto.response.SpecialiteMedResponseDto;
import org.springframework.data.domain.Page;

import org.mapstruct.Builder;
import org.mapstruct.Mapper;



@Mapper(componentModel = "spring" , builder = @Builder(disableBuilder = true))
public interface SpecialiteMedMapper {
    MedecinSpecialites dtoToModel(SpecialiteMedRequestDto specialiteMedRequestDto);
    SpecialiteMedResponseDto modelToDto(MedecinSpecialites medecinSpecialites);
    default Page<SpecialiteMedResponseDto> modelToDtos(Page<MedecinSpecialites> medecinSpecialitesPage)
    {
        return medecinSpecialitesPage.map(this::modelToDto);
    }

}
