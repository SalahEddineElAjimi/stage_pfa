package com.amasoft.amaclinic.controller;

import com.amasoft.amaclinic.Service.SalleService;
import com.amasoft.amaclinic.criteria.SalleCriteria;
import com.amasoft.amaclinic.dto.request.SalleRequestDTO;
import com.amasoft.amaclinic.dto.response.SalleResponceDTO;
import com.amasoft.amaclinic.exception.EntityNotFoundException;

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
@RequestMapping("/salles")
@AllArgsConstructor
public class SalleController {

    private final SalleService salleService;

    @GetMapping
    @Operation(summary = "Trouver des salles par critères", description = "Recherche des salles en fonction des critères fournis.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des salles trouvées."),
            @ApiResponse(responseCode = "400", description = "Requête invalide.")
    })
    public ResponseEntity<Page<SalleResponceDTO>> trouverSallesParCritere(
            @RequestParam(required = false) @Parameter(description = "ID de la salle") Long id,
            @RequestParam(required = false) @Parameter(description = "Code de la salle") String codeSalle,
            @RequestParam(required = false) @Parameter(description = "Nom de la salle") String nomSalle,
            @RequestParam(required = false) @Parameter(description = "Type de la salle") String type,
            @RequestParam(required = false) @Parameter(description = "Code du cabinet associé") String codeCabinet,
            @RequestParam(defaultValue = "0") @Parameter(description = "Numéro de page") int page,
            @RequestParam(defaultValue = "10") @Parameter(description = "Taille de la page") int size) {

        SalleCriteria salleCriteria = new SalleCriteria(id, codeSalle, nomSalle, type, codeCabinet);
        Page<SalleResponceDTO> result = salleService.trouverSallesParCritere(salleCriteria, page, size);
        return ResponseEntity.ok(result);
    }

    @PostMapping
    @Operation(summary = "Ajouter une salle", description = "Ajoute une nouvelle salle au système.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Salle ajoutée avec succès."),
            @ApiResponse(responseCode = "400", description = "Requête invalide.")
    })
    public ResponseEntity<SalleResponceDTO> ajouterSalle(
            @RequestBody @Parameter(description = "Détails de la salle à ajouter") SalleRequestDTO salleRequestDTO) {
        validateSalleRequestDTO(salleRequestDTO);
        SalleResponceDTO salleResponse = salleService.ajouterSalle(salleRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(salleResponse);
    }

    @PutMapping
    @Operation(summary = "Modifier une salle", description = "Modifie les détails d'une salle existante.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Salle modifiée avec succès."),
            @ApiResponse(responseCode = "400", description = "Requête invalide."),
            @ApiResponse(responseCode = "404", description = "Salle non trouvée.")
    })
    public ResponseEntity<SalleResponceDTO> modifierSalle(
            @RequestBody @Parameter(description = "Nouveaux détails de la salle") SalleRequestDTO salleRequestDTO) {
        validateSalleRequestDTO(salleRequestDTO);
        SalleResponceDTO salleResponse = salleService.modifierSalle(salleRequestDTO);
        return ResponseEntity.ok(salleResponse);
    }

    @DeleteMapping("/{codeSalle}")
    @Operation(summary = "Supprimer une salle", description = "Supprime une salle en fonction de son code.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Salle supprimée avec succès."),
            @ApiResponse(responseCode = "404", description = "Salle non trouvée.")
    })
    public ResponseEntity<Void> supprimerSalle(
            @PathVariable @Parameter(description = "code de la salle à supprimer") String codeSalle) {
        salleService.supprimerSalle(codeSalle);
        return ResponseEntity.noContent().build();
    }


    private void validateSalleRequestDTO(SalleRequestDTO dto) {
        if (dto.getCodeSalle() == null || dto.getCodeSalle().trim().isEmpty()) {
            throw new EntityNotFoundException("Le code de la salle ne peut pas être nul ou vide.");
        }
        if (dto.getCodeCabinet() == null || dto.getCodeCabinet().trim().isEmpty()) {
            throw new EntityNotFoundException("Le code du cabinet ne peut pas être nul ou vide.");
        }
    }

    @GetMapping("/download")
    public ResponseEntity<byte[]> downloadExcel()
    {
        try {
            byte[] excelFile = salleService.generateSalleExcel();
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=salles.xlsx");
            headers.add(HttpHeaders.CONTENT_TYPE, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            return new ResponseEntity<>(excelFile, headers , HttpStatus.OK);
        }catch (IOException e)
        {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
