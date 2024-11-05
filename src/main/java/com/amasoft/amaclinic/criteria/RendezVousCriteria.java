package com.amasoft.amaclinic.criteria;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RendezVousCriteria {
    private Long id;
    private String codeRendezVous;
    private LocalDateTime dateDebutRDV;
    private LocalDateTime dateFinRDV;
    private String status;
    private String typeRDV;
}
