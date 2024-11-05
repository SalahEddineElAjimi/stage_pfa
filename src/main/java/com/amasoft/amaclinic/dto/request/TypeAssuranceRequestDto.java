package com.amasoft.amaclinic.dto.request;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TypeAssuranceRequestDto {
    private Long id;
    private String codeAssuranceType;
    private String nomAssurance;
    private String typeAssurance;
    private String description;

}
