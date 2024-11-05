package com.amasoft.amaclinic.mapper;

import com.amasoft.amaclinic.Entity.Ordonnance;
import com.amasoft.amaclinic.dto.request.OrdonnanceRequestDTO;
import com.amasoft.amaclinic.dto.response.OrdonnanceResponseDTO;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface OrdonnanceMapper {

    @Mapping(source = "consultation", target = "consultation")
    @Mapping(source = "ordonnanceDetails", target = "ordonnanceDetails")
    OrdonnanceResponseDTO modelToDto(Ordonnance ordonnance);

    Ordonnance dtoToModel(OrdonnanceRequestDTO ordonnanceRequestDTO);

    default Page<OrdonnanceResponseDTO> modelToDtos(Page<Ordonnance> ordonnancePage) {
        return ordonnancePage.map(this::modelToDto);
    }
}
