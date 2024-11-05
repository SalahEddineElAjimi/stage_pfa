package com.amasoft.amaclinic.mapper;

import com.amasoft.amaclinic.Entity.ResultatConsultation;
import com.amasoft.amaclinic.dto.request.ResultatConsltRequestDTO;
import com.amasoft.amaclinic.dto.response.ResultatConsltResponseDTO;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))

public interface ResultatConsultMapper {
    ResultatConsultation dtoToModel(ResultatConsltRequestDTO resultatConsltRequestDTO);
    ResultatConsltResponseDTO modelToDto(ResultatConsultation resultatConsultation);
    default Page<ResultatConsltResponseDTO> modelToDtos(Page<ResultatConsultation> resultatConsultations)
    {
        return resultatConsultations.map(this::modelToDto);
    }
}
