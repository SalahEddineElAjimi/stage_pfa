package com.amasoft.amaclinic.mapper;


import com.amasoft.amaclinic.Entity.Consultation;
import com.amasoft.amaclinic.dto.request.ConsultationRequestDto;
import com.amasoft.amaclinic.dto.response.ConsultationResponseDTO;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface ConsultationMapper {
    Consultation dtoToModel(ConsultationRequestDto consultationRequestDto);

    @Mapping(source = "rendezVous", target = "rendezVousResponseDto")
    @Mapping(source = "paiment", target = "paiment")
    ConsultationResponseDTO modelToDto(Consultation consultation);

    default Page<ConsultationResponseDTO> modelToDtos(Page<Consultation> consultationPage) {
        return consultationPage.map(this::modelToDto);
    }

    ConsultationResponseDTO toResponseDto(Consultation consultation);
}
