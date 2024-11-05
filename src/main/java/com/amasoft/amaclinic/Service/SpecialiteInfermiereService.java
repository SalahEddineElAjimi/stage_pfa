package com.amasoft.amaclinic.Service;

import com.amasoft.amaclinic.criteria.SpecialiteInfeCriteria;
import com.amasoft.amaclinic.dto.request.SpecialiteInfeRequestDto;
import com.amasoft.amaclinic.dto.response.SpecialiteInfeResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public interface SpecialiteInfermiereService {

    Page<SpecialiteInfeResponseDto> trouverSpecialitesParCritere(SpecialiteInfeCriteria specialiteInfeCriteria, int page, int size);

    SpecialiteInfeResponseDto ajouterSpecialite(SpecialiteInfeRequestDto specialiteInfeRequestDto);

    SpecialiteInfeResponseDto modifierSpecialite(SpecialiteInfeRequestDto specialiteInfeRequestDto);

    void supprimerSpecialite(String codeSpecialite);
    byte[] generateExcelSpeInfe() throws IOException;
}
