package com.amasoft.amaclinic.controller;

import com.amasoft.amaclinic.dto.request.CentreRequestDTO;
import com.amasoft.amaclinic.dto.response.CentreResponseDTO;
import com.amasoft.amaclinic.criteria.CentreCriteria;
import com.amasoft.amaclinic.Service.CentreService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.Parameter;


import java.io.IOException;


@RestController
@RequestMapping("/centres")
@AllArgsConstructor
public class CentreController {

    private final CentreService centreService;

    @GetMapping
    @Operation(summary = "Rechercher des centres", description = "Recherche des centres en fonction des critères fournis.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des centres trouvés."),
            @ApiResponse(responseCode = "400", description = "Requête invalide.")
    })
    public ResponseEntity<Page<CentreResponseDTO>> searchCentres(
            @RequestParam(required = false) @Parameter(description = "ID du centre") Long id,
            @RequestParam(required = false) @Parameter(description = "Code du centre") String codeCentre,
            @RequestParam(required = false) @Parameter(description = "Nom du centre") String nomCentre,
            @RequestParam(required = false) @Parameter(description = "Adresse du centre") String adresse,
            @RequestParam(required = false) @Parameter(description = "Type du centre") String typeCentre,
            @RequestParam(required = false) @Parameter(description = "Téléphone du centre") String telephone,
            @RequestParam(defaultValue = "0") @Parameter(description = "Numéro de page") int page,
            @RequestParam(defaultValue = "10") @Parameter(description = "Taille de la page") int size) {

        CentreCriteria criteria = new CentreCriteria();
        criteria.setId(id);
        criteria.setCodeCentre(codeCentre);
        criteria.setNomCentre(nomCentre);
        criteria.setAdresse(adresse);
        criteria.setTypeCentre(typeCentre);
        criteria.setTelephone(telephone);

        Page<CentreResponseDTO> result = centreService.trouverCentresParCritere(criteria, page, size);
        return ResponseEntity.ok(result);
    }

    @PostMapping
    @Operation(summary = "Créer un centre", description = "Ajoute un nouveau centre.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Centre ajouté avec succès."),
            @ApiResponse(responseCode = "400", description = "Requête invalide.")
    })
    public ResponseEntity<CentreResponseDTO> createCentre(
            @RequestBody @Parameter(description = "Détails du centre à ajouter") CentreRequestDTO centreRequestDTO) {
        CentreResponseDTO response = centreService.ajouterCentre(centreRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping
    @Operation(summary = "Modifier un centre", description = "Modifie un centre existant.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Centre modifié avec succès."),
            @ApiResponse(responseCode = "400", description = "Requête invalide."),
            @ApiResponse(responseCode = "404", description = "Centre non trouvé.")
    })
    public ResponseEntity<CentreResponseDTO> updateCentre(
            @RequestBody @Parameter(description = "Détails du centre à modifier") CentreRequestDTO centreRequestDTO) {
        CentreResponseDTO response = centreService.modifierCentre(centreRequestDTO);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{codeCentre}")
    @Operation(summary = "Supprimer un centre", description = "Supprime un centre existant.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Centre supprimé avec succès."),
            @ApiResponse(responseCode = "404", description = "Centre non trouvé.")
    })
    public ResponseEntity<Void> deleteCentre(
                @PathVariable @Parameter(description = "Code du centre à supprimer") String codeCentre) {
        centreService.supprimerCentre(codeCentre);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/download")
    public ResponseEntity<byte[]> downloadExcel()
    {
        try {
            byte[] excelFile = centreService.generateCentreExcel();
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=centres.xlsx");
            headers.add(HttpHeaders.CONTENT_TYPE, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            return new ResponseEntity<>(excelFile, headers , HttpStatus.OK);
        }catch (IOException e)
        {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



}
