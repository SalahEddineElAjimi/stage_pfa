package com.amasoft.amaclinic.Service;

import com.amasoft.amaclinic.criteria.AbsenceInfermiereCriteria;
import com.amasoft.amaclinic.dto.request.AbsenceInfermiereRequestDTO;
import com.amasoft.amaclinic.dto.response.AbsenceInfermiereResponseDTO;
import org.springframework.data.domain.Page;

import java.io.IOException;

public interface AbsenceInfermiereService {
    AbsenceInfermiereResponseDTO ajouterAbsence(AbsenceInfermiereRequestDTO absenceRequestDTO);
    AbsenceInfermiereResponseDTO modifierAbsence(AbsenceInfermiereRequestDTO absenceRequestDTO);
    void supprimerAbsence(String codeAbsence);
    Page<AbsenceInfermiereResponseDTO> trouverAbsencesParCritere(AbsenceInfermiereCriteria criteria, int page, int size);
    public byte[] generateAbsInferExcel() throws IOException;
}
