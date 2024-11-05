package com.amasoft.amaclinic.mapper;

import com.amasoft.amaclinic.Entity.Equipement;
import com.amasoft.amaclinic.Entity.Salle;
import com.amasoft.amaclinic.dto.request.EquipementRequestDTO;
import com.amasoft.amaclinic.dto.response.EquipementResponseDTO;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface EquipementMapper {

    @Mapping(source = "codeEquipement", target = "codeEquipement")
    @Mapping(source = "nomEquipement", target = "nomEquipement")
    Equipement toEntity(EquipementRequestDTO dto);

    @Mapping(source = "codeEquipement", target = "codeEquipement")
    @Mapping(source = "nomEquipement", target = "nomEquipement")
    Equipement toEntity(EquipementRequestDTO dto, @MappingTarget Equipement entity);

    @Mapping(source = "codeEquipement", target = "codeEquipement")
    @Mapping(source = "nomEquipement", target = "nomEquipement")
    EquipementResponseDTO toDto(Equipement entity);


    default Page<EquipementResponseDTO> toDtoPage(Page<Equipement> entityPage) {
        return entityPage.map(this::toDto);
    }
}
