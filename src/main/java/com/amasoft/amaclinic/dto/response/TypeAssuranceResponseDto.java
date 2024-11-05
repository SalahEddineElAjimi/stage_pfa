package com.amasoft.amaclinic.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TypeAssuranceResponseDto {
    private String codeAssuranceType;
    private String nomAssurance;
    private String typeAssurance;
    private String description;

}
