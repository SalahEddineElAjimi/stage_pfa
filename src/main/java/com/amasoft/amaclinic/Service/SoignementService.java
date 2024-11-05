package com.amasoft.amaclinic.Service;

import com.amasoft.amaclinic.criteria.SoignementCriteria;
import com.amasoft.amaclinic.dto.request.SoignementRequestDTO;
import com.amasoft.amaclinic.dto.response.SoignementResponseDTO;
import org.springframework.data.domain.Page;

import java.io.IOException;

public interface SoignementService {
    SoignementResponseDTO addSoignement(SoignementRequestDTO soignementRequestDTO);
    SoignementResponseDTO updateSoignement(SoignementRequestDTO soignementRequestDTO);
    void deleteSoignement(String codeSoignement);
    Page<SoignementResponseDTO> findSoignementByCriteria(SoignementCriteria soignementCriteria, int page , int size);
byte[] generateSoignement() throws IOException;
}
