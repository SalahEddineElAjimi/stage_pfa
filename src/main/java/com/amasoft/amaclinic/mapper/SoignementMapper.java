package com.amasoft.amaclinic.mapper;

import com.amasoft.amaclinic.Entity.Soignement;
import com.amasoft.amaclinic.dto.request.SoignementRequestDTO;
import com.amasoft.amaclinic.dto.response.SoignementResponseDTO;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))

public interface SoignementMapper {
    Soignement dtoToModel(SoignementRequestDTO soignementRequestDTO);
    @Mapping(source = "patient" , target = "patient")
    @Mapping(source = "infermiere" , target = "infermiere")
    SoignementResponseDTO modelToDto(Soignement soignement);

    default Page<SoignementResponseDTO> modelToDtos(Page<Soignement> soignements)
    {
        return soignements.map(this::modelToDto);
    }

}
