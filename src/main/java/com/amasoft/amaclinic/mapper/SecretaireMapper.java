package com.amasoft.amaclinic.mapper;


import com.amasoft.amaclinic.Entity.Secretaire;
import com.amasoft.amaclinic.dto.request.secretaireRequestDto;
import com.amasoft.amaclinic.dto.response.responseSecretaire;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface SecretaireMapper {
    Secretaire dtoToModel(secretaireRequestDto secretaireRequestDto);
    responseSecretaire modelToDto(Secretaire secretaire);
    default Page<responseSecretaire> modelToDtos(Page<Secretaire> secretairePage)
    {
        return secretairePage.map(this::modelToDto);
    }
}

