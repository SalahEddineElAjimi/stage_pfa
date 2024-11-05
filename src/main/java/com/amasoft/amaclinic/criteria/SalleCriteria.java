package com.amasoft.amaclinic.criteria;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SalleCriteria {

    private Long id;
    private String codeSalle;
    private String nomSalle;
    private String type;
    private String codeCabinet;
}
