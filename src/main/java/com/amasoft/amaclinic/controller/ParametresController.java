package com.amasoft.amaclinic.controller;

import com.amasoft.amaclinic.Service.ParametresVService;
import com.amasoft.amaclinic.criteria.ParametresVCriteria;
import com.amasoft.amaclinic.dto.request.ParametresVRequestDto;
import com.amasoft.amaclinic.dto.response.ParametresVResponseDto;
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
@RequestMapping("/parametres")
public class ParametresController {

    private final ParametresVService parametresVService;

    @PostMapping
    @Operation(summary = "Créer un paramètre", description = "Crée un nouveau paramètre de santé.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Paramètre créé avec succès."),
            @ApiResponse(responseCode = "400", description = "Requête invalide.")
    })
    public ParametresVResponseDto save(
            @RequestBody @Parameter(description = "Détails du paramètre à créer") ParametresVRequestDto parametresVRequestDto) {
        return parametresVService.addParametres(parametresVRequestDto);
    }

    @PutMapping
    @Operation(summary = "Mettre à jour un paramètre", description = "Met à jour les détails d'un paramètre de santé existant.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Paramètre mis à jour avec succès."),
            @ApiResponse(responseCode = "400", description = "Requête invalide."),
            @ApiResponse(responseCode = "404", description = "Paramètre non trouvé.")
    })
    public ParametresVResponseDto update(
            @RequestBody @Parameter(description = "Nouveaux détails du paramètre") ParametresVRequestDto parametresVRequestDto) {
        return parametresVService.updateParametres(parametresVRequestDto);
    }

    @DeleteMapping
    @Operation(summary = "Supprimer un paramètre", description = "Supprime un paramètre de santé basé sur le code.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Paramètre supprimé avec succès."),
            @ApiResponse(responseCode = "404", description = "Paramètre non trouvé.")
    })
    public void delete(
            @RequestParam(name = "code") @Parameter(description = "Code du paramètre à supprimer") String code) {
        parametresVService.deleteParametres(code);
    }

    @GetMapping
    @Operation(summary = "Obtenir des paramètres par critères", description = "Recherche des paramètres de santé en fonction des critères fournis.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des paramètres trouvés."),
            @ApiResponse(responseCode = "400", description = "Requête invalide.")
    })
    public Page<ParametresVResponseDto> getParametreByCriteria(
            @RequestParam(defaultValue = "0", name = "page") @Parameter(description = "Numéro de page") int page,
            @RequestParam(defaultValue = "10", name = "size") @Parameter(description = "Taille de la page") int size,
            @RequestParam(name = "id", required = false) @Parameter(description = "ID du paramètre") Long id,
            @RequestParam(name = "poids", required = false) @Parameter(description = "Poids du patient") String poids,
            @RequestParam(name = "hauteur", required = false) @Parameter(description = "Hauteur du patient") String hauteur,
            @RequestParam(name = "parametreCode", required = false) @Parameter(description = "Code du paramètre") String parametreCode) {

        ParametresVCriteria parametresVCriteria = new ParametresVCriteria();
        parametresVCriteria.setId(id);
        parametresVCriteria.setPoids(poids);
        parametresVCriteria.setHauteur(hauteur);
        parametresVCriteria.setParametreCode(parametreCode);
        return parametresVService.findParametreByCriteria(parametresVCriteria, page, size);
    }

    @GetMapping("/download")
    public ResponseEntity<byte[]> downloadExcel()
    {
        try {
            byte[] excelFile = parametresVService.generateParametreV();
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=parametreV.xlsx");
            headers.add(HttpHeaders.CONTENT_TYPE, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            return new ResponseEntity<>(excelFile, headers , HttpStatus.OK);
        }catch (IOException e)
        {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
