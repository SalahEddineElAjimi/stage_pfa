package com.amasoft.amaclinic.mapper;

import com.amasoft.amaclinic.Entity.AbsenceInfermiere;
import com.amasoft.amaclinic.dto.request.AbsenceInfermiereRequestDTO;
import com.amasoft.amaclinic.dto.response.AbsenceInfermiereResponseDTO;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface AbscenceInfermiereMapper {

    @Mapping(target = "infermiere", ignore = true)  // Ignorer car l'infirmière sera assignée après
    AbsenceInfermiere dtoToModel(AbsenceInfermiereRequestDTO absenceRequestDTO);

    @Mapping(source = "infermiere.idInfermiere", target = "idInfermiere")
    AbsenceInfermiereResponseDTO modelToDto(AbsenceInfermiere absence);

    default Page<AbsenceInfermiereResponseDTO> modelToDtos(Page<AbsenceInfermiere> absencePage) {
        return absencePage.map(this::modelToDto);
    }
}
