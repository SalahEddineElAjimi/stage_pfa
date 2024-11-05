package com.amasoft.amaclinic.controller;

import com.amasoft.amaclinic.Service.ConsultationService;
import com.amasoft.amaclinic.criteria.ConsultationCriteria;
import com.amasoft.amaclinic.dto.request.ConsultationRequestDto;
import com.amasoft.amaclinic.dto.response.ConsultationResponseDTO;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.Parameter;

@RestController
@AllArgsConstructor
@RequestMapping("/consultation")
public class ConsultationController {
    private final ConsultationService consultationService;

    @PostMapping
    @Operation(summary = "Créer une consultation", description = "Ajoute une nouvelle consultation.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Consultation créée avec succès."),
            @ApiResponse(responseCode = "400", description = "Requête invalide.")
    })
    public ConsultationResponseDTO save(
            @RequestBody @Parameter(description = "Détails de la consultation à ajouter") ConsultationRequestDto consultationRequestDto) {
        return consultationService.addConsultation(consultationRequestDto);
    }

    @PutMapping
    @Operation(summary = "Mettre à jour une consultation", description = "Modifie une consultation existante.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Consultation mise à jour avec succès."),
            @ApiResponse(responseCode = "400", description = "Requête invalide."),
            @ApiResponse(responseCode = "404", description = "Consultation non trouvée.")
    })
    public ConsultationResponseDTO update(
            @RequestBody @Parameter(description = "Nouveaux détails de la consultation") ConsultationRequestDto consultationRequestDto) {
        return consultationService.UpdateConsultation(consultationRequestDto);
    }

    @DeleteMapping
    @Operation(summary = "Supprimer une consultation", description = "Supprime une consultation existante.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Consultation supprimée avec succès."),
            @ApiResponse(responseCode = "404", description = "Consultation non trouvée.")
    })
    public void delete(
            @RequestParam(name = "code") @Parameter(description = "Code de la consultation à supprimer") String code) {
        consultationService.deleteConsultation(code);
    }

    @GetMapping
    @Operation(summary = "Trouver des consultations par critères", description = "Recherche des consultations en fonction des critères fournis.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des consultations trouvées."),
            @ApiResponse(responseCode = "400", description = "Requête invalide.")
    })
    public Page<ConsultationResponseDTO> getConsultationByCriteria(
            @RequestParam(defaultValue = "0") @Parameter(description = "Numéro de page") int page,
            @RequestParam(defaultValue = "10") @Parameter(description = "Taille de la page") int size,
            @RequestParam(name = "id", required = false) @Parameter(description = "ID de la consultation") Long id,
            @RequestParam(name = "date", required = false) @Parameter(description = "Date de la consultation") LocalDateTime date,
            @RequestParam(name = "consultationCode", required = false) @Parameter(description = "Code de la consultation") String consultationCode) {

        ConsultationCriteria consultationCriteria = new ConsultationCriteria();
        consultationCriteria.setId(id);
        consultationCriteria.setDate(date);
        consultationCriteria.setCodeConsultation(consultationCode);
        return consultationService.findConsultationByCriteria(consultationCriteria, page, size);
    }

    @GetMapping("/download")
    public ResponseEntity<byte[]> downloadExcel()
    {
        try {
            byte[] excelFile = consultationService.generateConsultation();
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=medecins.xlsx");
            headers.add(HttpHeaders.CONTENT_TYPE, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            return new ResponseEntity<>(excelFile, headers , HttpStatus.OK);
        }catch (IOException e)
        {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
