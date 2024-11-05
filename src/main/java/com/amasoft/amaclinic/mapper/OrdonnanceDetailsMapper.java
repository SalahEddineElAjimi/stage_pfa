package com.amasoft.amaclinic.mapper;

import com.amasoft.amaclinic.Entity.OrdonnanceDetails;
import com.amasoft.amaclinic.dto.request.OrdonnanceDetailsRequestDTO;
import com.amasoft.amaclinic.dto.response.OrdonnanceDetailsResponseDTO;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface OrdonnanceDetailsMapper {

    OrdonnanceDetails dtoToModel(OrdonnanceDetailsRequestDTO ordonnanceDetailsDetailsDetailsRequestDTO);
    OrdonnanceDetailsResponseDTO modelToDto(OrdonnanceDetails ordonnanceDetailsDetailsDetails);

    default Page<OrdonnanceDetailsResponseDTO> modelToDtos(Page<OrdonnanceDetails> ordonnanceDetailsPage) {
        return ordonnanceDetailsPage.map(this::modelToDto);
    }
}
