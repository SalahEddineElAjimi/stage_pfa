package com.amasoft.amaclinic.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RendezVousResponseDTO {
    private String codeRendezVous;
    private LocalDateTime dateDebutRDV;
    private LocalDateTime dateFinRDV;
    private String status;
    private String typeRDV;


}
