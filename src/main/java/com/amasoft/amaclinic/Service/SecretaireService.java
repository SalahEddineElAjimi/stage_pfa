package com.amasoft.amaclinic.Service;

import com.amasoft.amaclinic.criteria.secretaireCriteria;
import com.amasoft.amaclinic.dto.request.secretaireRequestDto;
import com.amasoft.amaclinic.dto.response.responseSecretaire;
import org.springframework.data.domain.Page;

import java.io.IOException;

public interface SecretaireService {
    Page<responseSecretaire> trouverSecretaireByCriteria(secretaireCriteria secretaireCriteria , int page , int size);

    responseSecretaire addSecretaire(secretaireRequestDto secretaire);
    void deleteSecretaire(String CodeSecretaire);
    responseSecretaire updateSecretaire(secretaireRequestDto secretaire);
    byte[] generateSercretaireExcel()throws IOException;
}
