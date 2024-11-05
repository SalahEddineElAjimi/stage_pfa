package com.amasoft.amaclinic.Service;

import com.amasoft.amaclinic.criteria.CabinetCriteria;
import com.amasoft.amaclinic.dto.request.CabinetRequestDTO;
import com.amasoft.amaclinic.dto.response.CabinetResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public interface CabinetService {

    Page<CabinetResponseDTO> trouverCabinetsParCritere(CabinetCriteria cabinetCriteria ,int page , int size);

    CabinetResponseDTO ajouterCabinet(CabinetRequestDTO cabinetRequestDTO);

    CabinetResponseDTO modifierCabinet(CabinetRequestDTO cabinetRequestDTO);

    void supprimerCabinet(String codeCabinet);

    CabinetResponseDTO trouverCabinetParCodeCabinet(String codeCabinet);
    public byte[] generateCabinetExcel() throws IOException;

}
