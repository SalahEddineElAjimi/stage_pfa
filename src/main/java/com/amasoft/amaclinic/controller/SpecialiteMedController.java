package com.amasoft.amaclinic.controller;

import com.amasoft.amaclinic.Service.SpecialiteMedService;
import com.amasoft.amaclinic.criteria.SpecialiteCriteria;
import com.amasoft.amaclinic.dto.request.SpecialiteMedRequestDto;
import com.amasoft.amaclinic.dto.response.SpecialiteMedResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@AllArgsConstructor
@RequestMapping("/specialitesMedecin")
public class SpecialiteMedController {

    private final SpecialiteMedService specialiteMedService;

    @PostMapping
    @Operation(summary = "Ajouter une spécialité médicale", description = "Ajoute une nouvelle spécialité médicale au système.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Spécialité médicale ajoutée avec succès."),
            @ApiResponse(responseCode = "400", description = "Requête invalide.")
    })
    public SpecialiteMedResponseDto save(
            @RequestBody @Parameter(description = "Détails de la spécialité médicale à ajouter") SpecialiteMedRequestDto specialiteMedRequestDto) {
        return specialiteMedService.addSpecialite(specialiteMedRequestDto);
    }

    @PutMapping
    @Operation(summary = "Modifier une spécialité médicale", description = "Modifie les détails d'une spécialité médicale existante.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Spécialité médicale modifiée avec succès."),
            @ApiResponse(responseCode = "400", description = "Requête invalide."),
            @ApiResponse(responseCode = "404", description = "Spécialité médicale non trouvée.")
    })
    public SpecialiteMedResponseDto update(
            @RequestBody @Parameter(description = "Nouveaux détails de la spécialité médicale") SpecialiteMedRequestDto specialiteMedRequestDto) {
        return specialiteMedService.updateSpecialite(specialiteMedRequestDto);
    }

    @DeleteMapping
    @Operation(summary = "Supprimer une spécialité médicale", description = "Supprime une spécialité médicale en fonction du code fourni.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Spécialité médicale supprimée avec succès."),
            @ApiResponse(responseCode = "404", description = "Spécialité médicale non trouvée.")
    })
    public void delete(
            @RequestParam(name = "code") @Parameter(description = "Code de la spécialité médicale à supprimer") String code) {
        specialiteMedService.deleteSpecialite(code);
    }

    @GetMapping
    @Operation(summary = "Obtenir les spécialités médicales", description = "Recherche les spécialités médicales en fonction des critères fournis.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des spécialités médicales trouvées."),
            @ApiResponse(responseCode = "400", description = "Requête invalide.")
    })
    public Page<SpecialiteMedResponseDto> getSpecialiteBycriteria(
            @RequestParam(defaultValue = "0", name = "page") @Parameter(description = "Numéro de page") int page,
            @RequestParam(defaultValue = "10", name = "size") @Parameter(description = "Taille de la page") int size,
            @RequestParam(name = "id", required = false) @Parameter(description = "ID de la spécialité") Long id,
            @RequestParam(name = "codeSpecialite", required = false) @Parameter(description = "Code de la spécialité") String codeSpecialite,
            @RequestParam(name = "nom", required = false) @Parameter(description = "Nom de la spécialité") String nomSpecialite) {

        SpecialiteCriteria specialiteCriteria = new SpecialiteCriteria();
        specialiteCriteria.setId(id);
        specialiteCriteria.setCodeSpecialite(codeSpecialite);
        specialiteCriteria.setNom(nomSpecialite);

        return specialiteMedService.findSpecialiteByCriteria(specialiteCriteria, page, size);
    }

    @GetMapping("/download")
    public ResponseEntity<byte[]> downloadExcel()
    {
        try {
            byte[] excelFile = specialiteMedService.generateExcel();
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
