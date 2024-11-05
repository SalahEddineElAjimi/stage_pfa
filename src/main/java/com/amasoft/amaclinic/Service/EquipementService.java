package com.amasoft.amaclinic.Service;

import com.amasoft.amaclinic.criteria.EquipementCriteria;
import com.amasoft.amaclinic.dto.request.EquipementRequestDTO;


import com.amasoft.amaclinic.dto.response.EquipementResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public interface EquipementService {

    Page<EquipementResponseDTO> trouverEquipementsParCritere(EquipementCriteria equipementCriteria, int page, int size);

    EquipementResponseDTO ajouterEquipement(EquipementRequestDTO equipementRequestDTO);

    EquipementResponseDTO modifierEquipement(EquipementRequestDTO equipementRequestDTO);

    void supprimerEquipement(String codeEquipement);


    byte[] generateEquipementExcel() throws IOException;
}
