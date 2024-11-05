package com.amasoft.amaclinic.Service;

import com.amasoft.amaclinic.criteria.ResultatConsltCriteria;
import com.amasoft.amaclinic.dto.request.ResultatConsltRequestDTO;
import com.amasoft.amaclinic.dto.response.ResultatConsltResponseDTO;
import org.springframework.data.domain.Page;

import java.io.IOException;

public interface ResultatConsultService {
    ResultatConsltResponseDTO addResultatConsultation(ResultatConsltRequestDTO resultatConsltRequestDTO);
    ResultatConsltResponseDTO updateResultatConsultation(ResultatConsltRequestDTO resultatConsltRequestDTO);
    void deleteResultatConsultation(String codeConsultation);
    Page<ResultatConsltResponseDTO> findResultatConsultationByCriteria(ResultatConsltCriteria resultatConsltCriteria , int page , int size);
    byte[] generateResultatExcel()throws IOException;
}
