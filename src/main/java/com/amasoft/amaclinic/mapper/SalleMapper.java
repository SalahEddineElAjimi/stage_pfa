package com.amasoft.amaclinic.mapper;

import com.amasoft.amaclinic.Entity.Salle;
import com.amasoft.amaclinic.dto.request.SalleRequestDTO;
import com.amasoft.amaclinic.dto.response.SalleResponceDTO;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface SalleMapper {

    @Mapping(source = "codeSalle", target = "codeSalle")
    @Mapping(source = "nomSalle", target = "nomSalle")
    @Mapping(source = "type", target = "type")
    @Mapping(source = "codeCabinet", target = "cabinet.codeCabinet")
    Salle dtoToModel(SalleRequestDTO dto);

    @Mapping(source = "codeSalle", target = "codeSalle")
    @Mapping(source = "nomSalle", target = "nomSalle")
    @Mapping(source = "type", target = "type")
    @Mapping(source = "cabinet.codeCabinet", target = "codeCabinet")
    SalleResponceDTO modelToDto(Salle salle);

    default Page<SalleResponceDTO> modelToDtos(Page<Salle> sallePage) {
        return sallePage.map(this::modelToDto);
    }
}
