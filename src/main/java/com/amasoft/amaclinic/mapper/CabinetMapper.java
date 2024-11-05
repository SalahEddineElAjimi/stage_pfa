package com.amasoft.amaclinic.mapper;

import com.amasoft.amaclinic.Entity.Cabinet;
import com.amasoft.amaclinic.dto.request.CabinetRequestDTO;
import com.amasoft.amaclinic.dto.response.CabinetResponseDTO;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface CabinetMapper {

    Cabinet dtoToModel(CabinetRequestDTO cabinetRequestDTO);

    CabinetResponseDTO modelToDto(Cabinet cabinet);

    default Page<CabinetResponseDTO> modelToDtos(Page<Cabinet> cabinetPage) {
        return cabinetPage.map(this::modelToDto);
    }
}
