package com.amasoft.amaclinic.mapper;



import com.amasoft.amaclinic.Entity.RendezVous;
import com.amasoft.amaclinic.dto.request.RendezVousRequestDTO;
import com.amasoft.amaclinic.dto.response.RendezVousResponseDTO;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public interface RendezVousMapper {
    RendezVous dtoToModel(RendezVousRequestDTO rendezVousRequestDto);
    RendezVousResponseDTO modelToDto(RendezVous rendezVous);

    default Page<RendezVousResponseDTO> modelToDtos(Page<RendezVous> rendezVousPage) {
        return rendezVousPage.map(this::modelToDto);
    }

}
