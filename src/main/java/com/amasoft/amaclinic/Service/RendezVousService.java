package com.amasoft.amaclinic.Service;


import com.amasoft.amaclinic.criteria.RendezVousCriteria;
import com.amasoft.amaclinic.dto.request.RendezVousRequestDTO;
import com.amasoft.amaclinic.dto.response.RendezVousResponseDTO;
import org.springframework.data.domain.Page;

import java.io.IOException;

public interface RendezVousService {
    RendezVousResponseDTO addRendezVous(RendezVousRequestDTO rendezVousRequestDto);
    RendezVousResponseDTO updateRendezVous(RendezVousRequestDTO rendezVousRequestDTO);
    void  deleteRendezVous(String codeRDV);

    Page<RendezVousResponseDTO> findRendezVousByCriteria(RendezVousCriteria rendezVousCriteria, int page , int size);
    byte[] generateRdvExcel() throws IOException;




}
