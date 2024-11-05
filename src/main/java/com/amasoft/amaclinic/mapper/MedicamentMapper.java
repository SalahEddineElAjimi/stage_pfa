package com.amasoft.amaclinic.mapper;

import com.amasoft.amaclinic.Entity.Medicament;
import com.amasoft.amaclinic.dto.request.MedicamentRequestDTO;
import com.amasoft.amaclinic.dto.response.MedicamentResponseDTO;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface MedicamentMapper {

    Medicament dtoToModel(MedicamentRequestDTO medicamentRequestDto);
    MedicamentResponseDTO modelToDto(Medicament medicament);

    default Page<MedicamentResponseDTO> modelToDtos(Page<Medicament> medicamentPage) {
        return medicamentPage.map(this::modelToDto);
    }
}
