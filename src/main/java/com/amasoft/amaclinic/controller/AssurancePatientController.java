package com.amasoft.amaclinic.controller;

import com.amasoft.amaclinic.Service.AssurancePatientService;
import com.amasoft.amaclinic.criteria.AssurancePatientCriteria;
import com.amasoft.amaclinic.dto.request.AssurancePatientRequestDto;
import com.amasoft.amaclinic.dto.response.AssurancePatientResponseDto;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
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
@AllArgsConstructor
@RequestMapping("/assurancePatient")
public class AssurancePatientController {

    private final AssurancePatientService assurancePatientService;

    @PostMapping
    @Operation(summary = "Ajouter une assurance patient", description = "Ajoute une nouvelle assurance pour un patient.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Assurance patient ajoutée avec succès."),
            @ApiResponse(responseCode = "400", description = "Requête invalide.")
    })
    public AssurancePatientResponseDto save(
            @RequestBody @Parameter(description = "Détails de l'assurance patient à ajouter") AssurancePatientRequestDto assurancePatientRequestDto) {
        return assurancePatientService.addAssurancePatient(assurancePatientRequestDto);
    }

    @PutMapping
    @Operation(summary = "Modifier une assurance patient", description = "Modifie une assurance patient existante.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Assurance patient modifiée avec succès."),
            @ApiResponse(responseCode = "400", description = "Requête invalide."),
            @ApiResponse(responseCode = "404", description = "Assurance patient non trouvée.")
    })
    public AssurancePatientResponseDto update(
            @RequestBody @Parameter(description = "Détails de l'assurance patient à modifier") AssurancePatientRequestDto assurancePatientRequestDto) {
        return assurancePatientService.updateAssurancePatient(assurancePatientRequestDto);
    }

    @DeleteMapping
    @Operation(summary = "Supprimer une assurance patient", description = "Supprime une assurance patient existante.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Assurance patient supprimée avec succès."),
            @ApiResponse(responseCode = "404", description = "Assurance patient non trouvée.")
    })
    public void delete(
            @RequestParam(name = "code") @Parameter(description = "Code de l'assurance patient à supprimer") String code) {
        assurancePatientService.deleteAssurancePatient(code);
    }

    @GetMapping
    @Operation(summary = "Trouver des assurances patient par critères", description = "Recherche des assurances patient en fonction des critères fournis.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des assurances patient trouvées."),
            @ApiResponse(responseCode = "400", description = "Requête invalide.")
    })
    public Page<AssurancePatientResponseDto> getParametreByCriteria(
            @RequestParam(defaultValue = "0", name = "page") @Parameter(description = "Numéro de page") int page,
            @RequestParam(defaultValue = "10", name = "size") @Parameter(description = "Taille de la page") int size,
            @RequestParam(name = "id", required = false) @Parameter(description = "ID de l'assurance patient") Long id,
            @RequestParam(name = "numeroMatricule", required = false) @Parameter(description = "Numéro de matricule du patient") String numeroMatricule,
            @RequestParam(name = "dateDebutCouverture", required = false) @Parameter(description = "Date de début de couverture") LocalDate dateDebutCouverture,
            @RequestParam(name = "dateFinCouverture", required = false) @Parameter(description = "Date de fin de couverture") LocalDate dateFinCouverture,
            @RequestParam(name = "status", required = false) @Parameter(description = "Statut de l'assurance") String status,
            @RequestParam(name = "codeAssurance", required = false) @Parameter(description = "Code de l'assurance") String codeAssurance) {

        AssurancePatientCriteria assurancePatientCriteria = new AssurancePatientCriteria();
        assurancePatientCriteria.setId(id);
        assurancePatientCriteria.setNumeroMatricule(numeroMatricule);
        assurancePatientCriteria.setDateDebutCouverture(dateDebutCouverture);
        assurancePatientCriteria.setDateFinCouverture(dateFinCouverture);
        assurancePatientCriteria.setStatut(status);
        assurancePatientCriteria.setCodeAssurance(codeAssurance);

        return assurancePatientService.findAssurancePatientByCriteria(assurancePatientCriteria, page, size);
    }

    @GetMapping("/download")
    public ResponseEntity<byte[]> downloadExcel()
    {
        try {
            byte[] excelFile = assurancePatientService.generateExcel();
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=assurancePatient.xlsx");
            headers.add(HttpHeaders.CONTENT_TYPE, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            return new ResponseEntity<>(excelFile, headers , HttpStatus.OK);
        }catch (IOException e)
        {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
