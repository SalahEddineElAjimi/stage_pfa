package com.amasoft.amaclinic.mapper;


import com.amasoft.amaclinic.Entity.TypeAssurance;
import com.amasoft.amaclinic.dto.response.TypeAssuranceResponseDto;
import com.amasoft.amaclinic.dto.request.TypeAssuranceRequestDto;

import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface TypeAssuranceMapper {

    TypeAssurance dtoToModel(TypeAssuranceRequestDto typeAssuranceRequestDto);
    TypeAssuranceResponseDto modelToDto(TypeAssurance typeAssurance);

    default Page<TypeAssuranceResponseDto> modelToDtos(Page<TypeAssurance> typeAssurancePage) {
        return typeAssurancePage.map(this::modelToDto);
    }
}
