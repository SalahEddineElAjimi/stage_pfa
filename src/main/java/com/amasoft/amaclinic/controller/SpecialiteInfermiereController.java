package com.amasoft.amaclinic.controller;

import com.amasoft.amaclinic.Service.SpecialiteInfermiereService;
import com.amasoft.amaclinic.criteria.SpecialiteInfeCriteria;
import com.amasoft.amaclinic.dto.request.SpecialiteInfeRequestDto;
import com.amasoft.amaclinic.dto.response.SpecialiteInfeResponseDto;
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
@RequestMapping("/specialites")
public class SpecialiteInfermiereController {

    private final SpecialiteInfermiereService specialiteInfermiereService;

    @GetMapping
    @Operation(summary = "Obtenir les spécialités infirmières", description = "Recherche les spécialités infirmières en fonction des critères fournis.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des spécialités infirmières trouvées."),
            @ApiResponse(responseCode = "400", description = "Requête invalide.")
    })
    public Page<SpecialiteInfeResponseDto> getSpecialites(
            @RequestParam(defaultValue = "0", name = "page") @Parameter(description = "Numéro de page") int page,
            @RequestParam(defaultValue = "10", name = "size") @Parameter(description = "Taille de la page") int size,
            @RequestParam(name = "idSpecialite", required = false) @Parameter(description = "ID de la spécialité") Long idSpecialite,
            @RequestParam(name = "codeSpecialite", required = false) @Parameter(description = "Code de la spécialité") String codeSpecialite,
            @RequestParam(name = "nomSpecialite", required = false) @Parameter(description = "Nom de la spécialité") String nomSpecialite,
            @RequestParam(name = "infermiereCode", required = false) @Parameter(description = "Code de l'infirmière associée") String infermiereCode) {

        SpecialiteInfeCriteria criteria = new SpecialiteInfeCriteria();
        criteria.setIdSpecialite(idSpecialite);
        criteria.setCodeSpecialite(codeSpecialite);
        criteria.setNomSpecialite(nomSpecialite);
        criteria.setInfermiereCode(infermiereCode);

        return specialiteInfermiereService.trouverSpecialitesParCritere(criteria, page, size);
    }

    @PostMapping
    @Operation(summary = "Ajouter une spécialité infirmière", description = "Ajoute une nouvelle spécialité infirmière au système.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Spécialité infirmière ajoutée avec succès."),
            @ApiResponse(responseCode = "400", description = "Requête invalide.")
    })
    public SpecialiteInfeResponseDto save(
            @RequestBody @Parameter(description = "Détails de la spécialité infirmière à ajouter") SpecialiteInfeRequestDto specialiteInfeRequestDto) {
        return specialiteInfermiereService.ajouterSpecialite(specialiteInfeRequestDto);
    }

    @PutMapping
    @Operation(summary = "Modifier une spécialité infirmière", description = "Modifie les détails d'une spécialité infirmière existante.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Spécialité infirmière modifiée avec succès."),
            @ApiResponse(responseCode = "400", description = "Requête invalide."),
            @ApiResponse(responseCode = "404", description = "Spécialité infirmière non trouvée.")
    })
    public SpecialiteInfeResponseDto update(
            @RequestBody @Parameter(description = "Nouveaux détails de la spécialité infirmière") SpecialiteInfeRequestDto specialiteInfeRequestDto) {
        return specialiteInfermiereService.modifierSpecialite(specialiteInfeRequestDto);
    }

    @DeleteMapping
    @Operation(summary = "Supprimer une spécialité infirmière", description = "Supprime une spécialité infirmière en fonction de l'ID fourni.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Spécialité infirmière supprimée avec succès."),
            @ApiResponse(responseCode = "404", description = "Spécialité infirmière non trouvée.")
    })
    public void delete(@RequestParam(name = "code") String codeSpecialite) {
        specialiteInfermiereService.supprimerSpecialite(codeSpecialite);
    }

    @GetMapping("/download")
    public ResponseEntity<byte[]> downloadExcel()
    {
        try {
            byte[] excelFile = specialiteInfermiereService.generateExcelSpeInfe();
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=specialiteInfermiere.xlsx");
            headers.add(HttpHeaders.CONTENT_TYPE, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            return new ResponseEntity<>(excelFile, headers , HttpStatus.OK);
        }catch (IOException e)
        {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
