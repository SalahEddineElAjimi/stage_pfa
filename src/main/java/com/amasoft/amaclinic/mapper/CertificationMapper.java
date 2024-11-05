package com.amasoft.amaclinic.mapper;

import com.amasoft.amaclinic.Entity.Certification;
import com.amasoft.amaclinic.Entity.Consultation;
import com.amasoft.amaclinic.dto.request.CertificationRequestDto;
import com.amasoft.amaclinic.dto.response.CertificationResponseDTO;
import com.amasoft.amaclinic.dto.response.ConsultationResponseDTO;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.data.domain.Page;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring", builder = @Builder(disableBuilder = true))
public  interface CertificationMapper {

    @Autowired
    ConsultationMapper consultationMapper = null;

    @Mapping(source = "consultationCode", target = "consultation.id")
    public abstract Certification toEntity(CertificationRequestDto certificationRequestDto);

    @Mapping(target = "consultationResponseDto", source = "certification.consultation", qualifiedByName = "toConsultationResponseDto")
    public abstract CertificationResponseDTO toResponseDto(Certification certification);

    @Named("toConsultationResponseDto")
    public default ConsultationResponseDTO map(Consultation consultation) {
        return consultationMapper.toResponseDto(consultation);
    }

    public abstract Certification dtoToModel(CertificationRequestDto certificationRequestDto);

    public abstract CertificationRequestDto modelToDto(Certification certification);

    public default Page<CertificationResponseDTO> toResponseDtoPage(Page<Certification> certificationPage) {
        return certificationPage.map(this::toResponseDto);
    }
}
