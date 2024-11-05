package com.amasoft.amaclinic.Service;

import com.amasoft.amaclinic.Entity.Salle;
import com.amasoft.amaclinic.criteria.SalleCriteria;
import com.amasoft.amaclinic.dto.request.SalleRequestDTO;
import com.amasoft.amaclinic.dto.response.SalleResponceDTO;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import java.util.List;
import java.io.IOException;

@Service
public interface SalleService {


    Page<SalleResponceDTO> trouverSallesParCritere(SalleCriteria salleCriteria, int page, int size);


    SalleResponceDTO ajouterSalle(SalleRequestDTO salleRequestDTO);


    SalleResponceDTO modifierSalle(SalleRequestDTO salleRequestDTO);


    void supprimerSalle(String codeSalle);
    List<Salle> findByCodes(List<String> codes);
    Salle save(Salle salle);
    byte[] generateSalleExcel() throws IOException;
}
