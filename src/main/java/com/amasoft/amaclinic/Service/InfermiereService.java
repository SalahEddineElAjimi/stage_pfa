package com.amasoft.amaclinic.Service;

import com.amasoft.amaclinic.Entity.Infermiere;
import com.amasoft.amaclinic.criteria.InfermiereCriteria;
import com.amasoft.amaclinic.dto.request.InfermiereRequestDTO;
import com.amasoft.amaclinic.dto.response.InfermiereResponseDTO;
import org.springframework.data.domain.Page;

import java.io.IOException;
import java.util.Optional;


public interface InfermiereService {

    Page<InfermiereResponseDTO> trouverInfirmieresParCritere(InfermiereCriteria infermiereCriteria, int page, int size);

    InfermiereResponseDTO ajouterInfermiere(InfermiereRequestDTO infermiereRequestDto);

    InfermiereResponseDTO modifierInfermiere(InfermiereRequestDTO infermiereRequestDto);

    void supprimerInfermiere(String codeInferm);
    Optional<Infermiere> findByCodeInferm(String codeInferm);

    byte[] generateInfermiereExcel() throws IOException;
}
