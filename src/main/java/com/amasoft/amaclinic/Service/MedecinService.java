package com.amasoft.amaclinic.Service;

import com.amasoft.amaclinic.criteria.MedecinCriteria;
import com.amasoft.amaclinic.dto.request.MedecinRequestDto;
import com.amasoft.amaclinic.dto.response.MedecinResponseDto;
import jakarta.servlet.http.HttpServlet;
import org.springframework.data.domain.Page;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public interface MedecinService {
    MedecinResponseDto addMedecin(MedecinRequestDto medecinRequestDto);
    MedecinResponseDto UpdateMedecin(MedecinRequestDto medecinRequestDto);
    void  deleteMedecin(String codeMedecin);

    Page<MedecinResponseDto> findMedecinByCriteria(MedecinCriteria medecinCriteria, int page , int size);

     byte[] generateMedecinExcel() throws IOException;







}
