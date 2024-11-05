package com.amasoft.amaclinic.dto.request;

import jakarta.persistence.Column;
import jakarta.persistence.PrimaryKeyJoinColumn;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RendezVousRequestDTO {

    private Long id;
    private String codeRendezVous;
    private String status;
    private String typeRDV;
    private LocalDateTime dateDebutRDV;
    private LocalDateTime dateFinRDV;
    private String codePatient;
    private String codeMedecin;

}
