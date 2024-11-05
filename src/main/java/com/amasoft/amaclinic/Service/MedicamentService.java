package com.amasoft.amaclinic.Service;

import com.amasoft.amaclinic.criteria.MedicamentCriteria;
import com.amasoft.amaclinic.dto.request.MedicamentRequestDTO;
import com.amasoft.amaclinic.dto.response.MedicamentResponseDTO;
import org.springframework.data.domain.Page;

import java.io.IOException;

public interface MedicamentService {

    MedicamentResponseDTO addMedicament(MedicamentRequestDTO medicamentRequestDto);
    MedicamentResponseDTO updateMedicament(MedicamentRequestDTO medicamentRequestDTO);
    void  deleteMedicament(String codeMedicament);

    Page<MedicamentResponseDTO> findMedicamentByCriteria(MedicamentCriteria medicamentCriteria, int page , int size);
    byte[] generateMedecamentExcel() throws IOException;


}
