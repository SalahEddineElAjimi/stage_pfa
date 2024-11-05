package com.amasoft.amaclinic.mapper;

import com.amasoft.amaclinic.Entity.Infermiere;
import com.amasoft.amaclinic.dto.request.InfermiereRequestDTO;
import com.amasoft.amaclinic.dto.response.InfermiereResponseDTO;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface InfermiereMapper {

    @Mapping(source = "codeInferm", target = "codeInferm")
    @Mapping(source = "nom", target = "nom")
    @Mapping(source = "prenom", target = "prenom")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "ville", target = "ville")
    @Mapping(source = "cin", target = "cin")
    @Mapping(source = "adresse", target = "adresse")
    @Mapping(source = "telephone", target = "telephone")

    Infermiere dtoToModel(InfermiereRequestDTO dto);


    InfermiereResponseDTO modelToDto(Infermiere infermiere);


    default Page<InfermiereResponseDTO> modelToDtos(Page<Infermiere> infermierePage) {
        return infermierePage.map(this::modelToDto);
    }
}
