package com.amasoft.amaclinic.Service;

import com.amasoft.amaclinic.criteria.OrdonnanceCriteria;
import com.amasoft.amaclinic.dto.request.OrdonnanceRequestDTO;
import com.amasoft.amaclinic.dto.response.OrdonnanceResponseDTO;
import net.sf.jasperreports.engine.JRException;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

import java.io.IOException;

public interface OrdonnanceService {

    OrdonnanceResponseDTO addOrdonnance(OrdonnanceRequestDTO ordonnanceRequestDto);
    OrdonnanceResponseDTO updateOrdonnance(OrdonnanceRequestDTO ordonnanceRequestDTO);
    void  deleteOrdonnance(String codeOrdonnance);

    Page<OrdonnanceResponseDTO> findOrdonnanceByCriteria(OrdonnanceCriteria ordonnanceCriteria, int page , int size);

    ResponseEntity<byte[]> generateOrdonnanceReport(String code) throws JRException, IOException;

    byte[] generateOrdonnanceEXCEL() throws IOException;



}
