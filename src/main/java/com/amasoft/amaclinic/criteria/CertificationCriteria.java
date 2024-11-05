package com.amasoft.amaclinic.criteria;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CertificationCriteria {
    private String code;
    private String type;
    private String consultationCode;
    private LocalDateTime dateDebut;
    private LocalDateTime dateFin;
}
