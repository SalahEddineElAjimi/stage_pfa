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
public class PaimentCriteria {

    private String codePaiment;          // Critère pour filtrer par code de paiement
    private LocalDateTime datePaimentFrom; // Filtrer les paiements effectués après cette date
    private LocalDateTime datePaimentTo;   // Filtrer les paiements effectués avant cette date
    private Integer montantMin;            // Filtrer les paiements avec un montant minimum
    private Integer montantMax;            // Filtrer les paiements avec un montant maximum
    private String typePaiment;            // Filtrer par type de paiement
    private String codeConsultation;       // Filtrer par code de consultation associé
}
