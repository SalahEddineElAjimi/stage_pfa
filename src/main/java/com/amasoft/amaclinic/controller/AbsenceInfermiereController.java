package com.amasoft.amaclinic.controller;

import com.amasoft.amaclinic.Service.AbsenceInfermiereService;
import com.amasoft.amaclinic.dto.request.AbsenceInfermiereRequestDTO;
import com.amasoft.amaclinic.dto.response.AbsenceInfermiereResponseDTO;
import com.amasoft.amaclinic.criteria.AbsenceInfermiereCriteria;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.Parameter;

@RestController
@RequestMapping("/absences")
@AllArgsConstructor
public class AbsenceInfermiereController {

    private final AbsenceInfermiereService absenceService;

    @PostMapping
    @Transactional
    @Operation(summary = "Ajouter une absence", description = "Crée une nouvelle absence pour un infirmier.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Absence créée avec succès."),
            @ApiResponse(responseCode = "400", description = "Requête invalide.")
    })
    public ResponseEntity<AbsenceInfermiereResponseDTO> ajouterAbsence(
            @RequestBody @Parameter(description = "Détails de l'absence à ajouter") AbsenceInfermiereRequestDTO absenceRequestDTO) {
        AbsenceInfermiereResponseDTO responseDTO = absenceService.ajouterAbsence(absenceRequestDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @PutMapping("/{codeAbscence}")
    @Transactional
    @Operation(summary = "Modifier une absence", description = "Modifie une absence existante.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Absence modifiée avec succès."),
            @ApiResponse(responseCode = "404", description = "Absence non trouvée.")
    })
    public ResponseEntity<AbsenceInfermiereResponseDTO> modifierAbsence(
            @PathVariable @Parameter(description = "Code de l'absence à modifier") String codeAbscence,
            @RequestBody @Parameter(description = "Détails de l'absence à modifier") AbsenceInfermiereRequestDTO absenceRequestDTO) {
        absenceRequestDTO.setCodeAbscence(codeAbscence);
        AbsenceInfermiereResponseDTO responseDTO = absenceService.modifierAbsence(absenceRequestDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @DeleteMapping("/{codeAbsence}")
    @Transactional
    @Operation(summary = "Supprimer une absence", description = "Supprime une absence existante.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Absence supprimée avec succès."),
            @ApiResponse(responseCode = "404", description = "Absence non trouvée.")
    })
    public ResponseEntity<Void> supprimerAbsence(@RequestParam @Parameter(description = "code de l'absence à supprimer") String codeAbsence) {
        absenceService.supprimerAbsence(codeAbsence);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping
    @Operation(summary = "Trouver des absences par critère", description = "Recherche des absences en fonction des critères fournis.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des absences trouvées."),
            @ApiResponse(responseCode = "400", description = "Requête invalide.")
    })
    public ResponseEntity<Page<AbsenceInfermiereResponseDTO>> trouverAbsencesParCritere(
            @RequestParam(value = "idAbsence", required = false) @Parameter(description = "ID de l'absence") Long idAbsence,
            @RequestParam(value = "codeAbscence", required = false) @Parameter(description = "Code de l'absence") String codeAbscence,
            @RequestParam(value = "dateDebut", required = false) @Parameter(description = "Date de début de l'absence") String dateDebut,
            @RequestParam(value = "dateFin", required = false) @Parameter(description = "Date de fin de l'absence") String dateFin,
            @RequestParam(value = "idInfermiere", required = false) @Parameter(description = "ID de l'infirmier") Long idInfermiere,
            @RequestParam(value = "page", defaultValue = "0") @Parameter(description = "Numéro de page") int page,
            @RequestParam(value = "size", defaultValue = "10") @Parameter(description = "Taille de la page") int size) {

        AbsenceInfermiereCriteria criteria = AbsenceInfermiereCriteria.builder()
                .idAbsence(idAbsence)
                .codeAbscence(codeAbscence)
                .dateDebut(dateDebut != null ? LocalDate.parse(dateDebut) : null)
                .dateFin(dateFin != null ? LocalDate.parse(dateFin) : null)
                .idInfermiere(idInfermiere)
                .build();

        Pageable pageable = PageRequest.of(page, size);
        Page<AbsenceInfermiereResponseDTO> absences = absenceService.trouverAbsencesParCritere(criteria, page, size);
        return new ResponseEntity<>(absences, HttpStatus.OK);
    }

    @GetMapping("/download")
    public ResponseEntity<byte[]> downloadExcel()
    {
        try {
            byte[] excelFile = absenceService.generateAbsInferExcel();
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=absenceInfermiere.xlsx");
            headers.add(HttpHeaders.CONTENT_TYPE, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            return new ResponseEntity<>(excelFile, headers , HttpStatus.OK);
        }catch (IOException e)
        {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
