package com.amasoft.amaclinic.Service;

import com.amasoft.amaclinic.criteria.SpecialiteCriteria;
import com.amasoft.amaclinic.dto.request.SpecialiteMedRequestDto;
import com.amasoft.amaclinic.dto.response.SpecialiteMedResponseDto;
import org.springframework.data.domain.Page;

import java.io.IOException;

public interface SpecialiteMedService {
    SpecialiteMedResponseDto addSpecialite(SpecialiteMedRequestDto specialiteMedRequestDto);
    SpecialiteMedResponseDto updateSpecialite(SpecialiteMedRequestDto specialiteMedRequestDto);
    void deleteSpecialite(String codeSpecialite);
    Page<SpecialiteMedResponseDto> findSpecialiteByCriteria(SpecialiteCriteria SpecialiteCriteria, int page , int size);
    byte [] generateExcel() throws IOException;
}
