package com.amasoft.amaclinic.mapper;

import com.amasoft.amaclinic.Entity.Centre;
import com.amasoft.amaclinic.dto.request.CentreRequestDTO;
import com.amasoft.amaclinic.dto.response.CentreResponseDTO;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface CentreMapper {

    Centre dtoToModel(CentreRequestDTO centreRequestDTO);

    CentreResponseDTO modelToDto(Centre centre);

    default Page<CentreResponseDTO> modelToDtos(Page<Centre> centrePage) {
        return centrePage.map(this::modelToDto);
    }
}
