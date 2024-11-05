package com.amasoft.amaclinic.Service;


import com.amasoft.amaclinic.criteria.ParametresVCriteria;
import com.amasoft.amaclinic.dto.request.ParametresVRequestDto;
import com.amasoft.amaclinic.dto.response.ParametresVResponseDto;
import org.springframework.data.domain.Page;

import java.io.IOException;

public interface ParametresVService {
    ParametresVResponseDto addParametres(ParametresVRequestDto parametresVRequestDto);
    ParametresVResponseDto updateParametres(ParametresVRequestDto parametresVRequestDto);
    void  deleteParametres(String codeParametre);

    Page<ParametresVResponseDto> findParametreByCriteria(ParametresVCriteria parametresCriteria, int page , int size);
    byte[] generateParametreV() throws IOException;







}
