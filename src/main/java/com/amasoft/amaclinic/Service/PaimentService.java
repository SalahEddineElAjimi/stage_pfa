package com.amasoft.amaclinic.Service;

import com.amasoft.amaclinic.criteria.PaimentCriteria;
import com.amasoft.amaclinic.dto.request.PaimentRequestDto;
import com.amasoft.amaclinic.dto.response.PaimentResponseDto;
import net.sf.jasperreports.engine.JRException;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.Optional;

public interface PaimentService {

    PaimentResponseDto creerPaiment(PaimentRequestDto paimentRequestDto);

    Optional<PaimentResponseDto> obtenirPaimentParCode(String codePaiment);

    void supprimerPaimentParCode(String codePaiment);

    PaimentResponseDto mettreAJourPaiment(String codePaiment, PaimentRequestDto paimentRequestDto);

    Page<PaimentResponseDto> trouverPaimentParCritères(PaimentCriteria paimentCriteria, int page, int size);

    ResponseEntity<byte[]> generatePaymentReceiptReport(String code) throws JRException, IOException;

    byte [] generatePaiment() throws IOException;
}
