package com.amasoft.amaclinic.dto.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SpecialiteInfeRequestDto {

    private Long idSpecialite;
    private String codeSpecialite;
    private String nomSpecialite;
}
