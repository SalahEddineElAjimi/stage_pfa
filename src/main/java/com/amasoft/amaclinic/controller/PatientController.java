package com.amasoft.amaclinic.controller;

import com.amasoft.amaclinic.Service.PatientService;
import com.amasoft.amaclinic.criteria.PatientCriteria;
import com.amasoft.amaclinic.dto.request.PatientRequestDto;
import com.amasoft.amaclinic.dto.response.PatientResponseDto;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.io.IOException;

@RestController
@AllArgsConstructor
@RequestMapping("/patients")
public class PatientController {

    private final PatientService patientService;

    @PostMapping
    @Operation(summary = "Créer un patient", description = "Crée un nouveau patient dans le système.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Patient créé avec succès."),
            @ApiResponse(responseCode = "400", description = "Requête invalide.")
    })
    public PatientResponseDto save(
            @RequestBody @Parameter(description = "Détails du patient à créer") PatientRequestDto patientRequestDto) {
        return patientService.addPatient(patientRequestDto);
    }

    @PutMapping
    @Operation(summary = "Mettre à jour un patient", description = "Met à jour les détails d'un patient existant.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Patient mis à jour avec succès."),
            @ApiResponse(responseCode = "400", description = "Requête invalide."),
            @ApiResponse(responseCode = "404", description = "Patient non trouvé.")
    })
    public PatientResponseDto update(
            @RequestBody @Parameter(description = "Nouveaux détails du patient") PatientRequestDto patientRequestDto) {
        return patientService.updatePatient(patientRequestDto);
    }

    @DeleteMapping
    @Operation(summary = "Supprimer un patient", description = "Supprime un patient basé sur le code.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Patient supprimé avec succès."),
            @ApiResponse(responseCode = "404", description = "Patient non trouvé.")
    })
    public void delete(
            @RequestParam(name = "code") @Parameter(description = "Code du patient à supprimer") String code) {
        patientService.deletePatient(code);
    }

    @GetMapping
    @Operation(summary = "Obtenir des patients par critères", description = "Recherche des patients en fonction des critères fournis.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des patients trouvés."),
            @ApiResponse(responseCode = "400", description = "Requête invalide.")
    })
    public Page<PatientResponseDto> getPatientByCriteria(
            @RequestParam(defaultValue = "0", name = "page") @Parameter(description = "Numéro de page") int page,
            @RequestParam(defaultValue = "10", name = "size") @Parameter(description = "Taille de la page") int size,
            @RequestParam(name = "id", required = false) @Parameter(description = "ID du patient") Long id,
            @RequestParam(name = "prenom", required = false) @Parameter(description = "Prénom du patient") String prenom,
            @RequestParam(name = "nom", required = false) @Parameter(description = "Nom du patient") String nom,
            @RequestParam(name = "cin", required = false) @Parameter(description = "CIN du patient") String cin,
            @RequestParam(name = "patientCode", required = false) @Parameter(description = "Code du patient") String patientCode) {

        PatientCriteria patientCriteria = new PatientCriteria();
        patientCriteria.setId(id);
        patientCriteria.setPrenom(prenom);
        patientCriteria.setNom(nom);
        patientCriteria.setPatientCode(patientCode);
        patientCriteria.setCin(cin);
        return patientService.findPatientByCriteria(patientCriteria, page, size);
    }
    @GetMapping("/download")
    public ResponseEntity<byte[]> downloadExcel()
    {
        try {
            byte[] excelFile = patientService.generatePatientExcel();
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=patients.xlsx");
            headers.add(HttpHeaders.CONTENT_TYPE, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            return new ResponseEntity<>(excelFile, headers , HttpStatus.OK);
        }catch (IOException e)
        {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
