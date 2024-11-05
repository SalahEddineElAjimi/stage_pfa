package com.amasoft.amaclinic.Service;


import com.amasoft.amaclinic.Entity.Centre;
import com.amasoft.amaclinic.dto.request.CentreRequestDTO;
import com.amasoft.amaclinic.dto.response.CentreResponseDTO;
import com.amasoft.amaclinic.criteria.CentreCriteria;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public interface CentreService {

    Page<CentreResponseDTO> trouverCentresParCritere(CentreCriteria centreCriteria, int page, int size);

    CentreResponseDTO ajouterCentre(CentreRequestDTO centreRequestDTO);

    CentreResponseDTO modifierCentre(CentreRequestDTO centreRequestDTO);

    void supprimerCentre(String codeCentre);

    CentreResponseDTO trouverCentreParId(Long id);

    List<Centre> findByIds(List<Long> ids);

    List<Centre> findByCodes(List<String> codes);

    Centre save(Centre centre);
    public byte[] generateCentreExcel() throws IOException;



}
