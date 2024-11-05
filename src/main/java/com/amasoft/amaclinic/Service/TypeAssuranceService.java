package com.amasoft.amaclinic.Service;


import com.amasoft.amaclinic.criteria.TypeAssuranceCriteria;
import com.amasoft.amaclinic.dto.request.TypeAssuranceRequestDto;
import com.amasoft.amaclinic.dto.response.TypeAssuranceResponseDto;
import org.springframework.data.domain.Page;

import java.io.IOException;

public interface TypeAssuranceService {
    TypeAssuranceResponseDto addTypeAssurance(TypeAssuranceRequestDto typeAssuranceRequestDto);
    TypeAssuranceResponseDto updateTypeAssurance(TypeAssuranceRequestDto typeAssuranceRequestDto);
    void  deleteTypeAssurance(String codeTypeAssurance);

    Page<TypeAssuranceResponseDto> findTypeAssuranceByCriteria(TypeAssuranceCriteria typeAssuranceCriteria, int page , int size);
    byte[] generateTypeAssurance() throws IOException;

}
