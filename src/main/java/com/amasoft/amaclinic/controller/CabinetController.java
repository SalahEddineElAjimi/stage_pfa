package com.amasoft.amaclinic.controller;

import com.amasoft.amaclinic.Service.CabinetService;
import com.amasoft.amaclinic.criteria.CabinetCriteria;
import com.amasoft.amaclinic.dto.request.CabinetRequestDTO;
import com.amasoft.amaclinic.dto.response.CabinetResponseDTO;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
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
@AllArgsConstructor
@RequestMapping("/cabinets")
public class CabinetController {

    private final CabinetService cabinetService;

    @GetMapping
    @Operation(summary = "Trouver des cabinets par critères", description = "Recherche des cabinets en fonction des critères fournis.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des cabinets trouvés."),
            @ApiResponse(responseCode = "400", description = "Requête invalide.")
    })
    public Page<CabinetResponseDTO> getCabinetsByCriteria(
            @RequestParam(defaultValue = "0", name = "page") @Parameter(description = "Numéro de page") int page,
            @RequestParam(defaultValue = "10", name = "size") @Parameter(description = "Taille de la page") int size,
            @RequestParam(name = "id", required = false) @Parameter(description = "ID du cabinet") Long id,
            @RequestParam(name = "codeCabinet", required = false) @Parameter(description = "Code du cabinet") String codeCabinet,
            @RequestParam(name = "nomCabinet", required = false) @Parameter(description = "Nom du cabinet") String nomCabinet,
            @RequestParam(name = "adresse", required = false) @Parameter(description = "Adresse du cabinet") String adresse) {

        CabinetCriteria cabinetCriteria = new CabinetCriteria();
        cabinetCriteria.setId(id);
        cabinetCriteria.setCodeCabinet(codeCabinet);
        cabinetCriteria.setNomCabinet(nomCabinet);
        cabinetCriteria.setAdresse(adresse);

        return cabinetService.trouverCabinetsParCritere(cabinetCriteria, page, size);
    }

    @PostMapping
    @Operation(summary = "Ajouter un cabinet", description = "Ajoute un nouveau cabinet.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cabinet ajouté avec succès."),
            @ApiResponse(responseCode = "400", description = "Requête invalide.")
    })
    public CabinetResponseDTO save(
            @RequestBody @Parameter(description = "Détails du cabinet à ajouter") CabinetRequestDTO cabinetRequestDto) {
        return cabinetService.ajouterCabinet(cabinetRequestDto);
    }

    @PutMapping
    @Operation(summary = "Modifier un cabinet", description = "Modifie un cabinet existant.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cabinet modifié avec succès."),
            @ApiResponse(responseCode = "400", description = "Requête invalide."),
            @ApiResponse(responseCode = "404", description = "Cabinet non trouvé.")
    })
    public CabinetResponseDTO update(
            @RequestBody @Parameter(description = "Détails du cabinet à modifier") CabinetRequestDTO cabinetRequestDto) {
        return cabinetService.modifierCabinet(cabinetRequestDto);
    }

    @DeleteMapping("/{codeCabinet}")
    @Operation(summary = "Supprimer un cabinet", description = "Supprime un cabinet existant.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Cabinet supprimé avec succès."),
            @ApiResponse(responseCode = "404", description = "Cabinet non trouvé.")
    })
    public void delete(
            @PathVariable(name = "codeCabinet") @Parameter(description = "Code du cabinet à supprimer") String codeCabinet) {
        cabinetService.supprimerCabinet(codeCabinet);
    }


    @GetMapping("/download")
    public ResponseEntity<byte[]> downloadExcel()
    {
        try {
            byte[] excelFile = cabinetService.generateCabinetExcel();
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=cabinets.xlsx");
            headers.add(HttpHeaders.CONTENT_TYPE, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            return new ResponseEntity<>(excelFile, headers , HttpStatus.OK);
        }catch (IOException e)
        {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

