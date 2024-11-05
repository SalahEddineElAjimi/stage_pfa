package com.amasoft.amaclinic.Service;


import com.amasoft.amaclinic.criteria.ConsultationCriteria;
import com.amasoft.amaclinic.dto.request.ConsultationRequestDto;
import com.amasoft.amaclinic.dto.response.ConsultationResponseDTO;
import org.springframework.data.domain.Page;

import java.io.IOException;

public interface ConsultationService {

    ConsultationResponseDTO addConsultation(ConsultationRequestDto consultationRequestDto);
    ConsultationResponseDTO UpdateConsultation(ConsultationRequestDto consultationRequestDto);
    void  deleteConsultation(String codeConsultation);

    Page<ConsultationResponseDTO> findConsultationByCriteria(ConsultationCriteria consultationCriteria, int page , int size);
    public byte[] generateConsultation() throws IOException;


}
