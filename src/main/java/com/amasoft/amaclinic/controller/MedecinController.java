package com.amasoft.amaclinic.controller;

import com.amasoft.amaclinic.Service.MedecinService;
import com.amasoft.amaclinic.criteria.MedecinCriteria;
import com.amasoft.amaclinic.dto.request.MedecinRequestDto;
import com.amasoft.amaclinic.dto.response.MedecinResponseDto;
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
@RequestMapping("/medecins")
public class MedecinController {
    private final MedecinService medecinService;

    @PostMapping
    @Operation(summary = "Ajouter un médecin", description = "Ajoute un nouveau médecin au système.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Médecin créé avec succès."),
            @ApiResponse(responseCode = "400", description = "Requête invalide.")
    })
    public MedecinResponseDto save(
            @RequestBody @Parameter(description = "Détails du médecin à ajouter") MedecinRequestDto medecinRequestDto) {
        return medecinService.addMedecin(medecinRequestDto);
    }

    @PutMapping
    @Operation(summary = "Modifier un médecin", description = "Modifie les détails d'un médecin existant.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Médecin modifié avec succès."),
            @ApiResponse(responseCode = "400", description = "Requête invalide."),
            @ApiResponse(responseCode = "404", description = "Médecin non trouvé.")
    })
    public MedecinResponseDto update(
            @RequestBody @Parameter(description = "Nouveaux détails du médecin") MedecinRequestDto medecinRequestDto) {
        return medecinService.UpdateMedecin(medecinRequestDto);
    }

    @DeleteMapping
    @Operation(summary = "Supprimer un médecin", description = "Supprime un médecin basé sur le code.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Médecin supprimé avec succès."),
            @ApiResponse(responseCode = "404", description = "Médecin non trouvé.")
    })
    public void delete(
            @RequestParam(name = "code") @Parameter(description = "Code du médecin à supprimer") String code) {
        medecinService.deleteMedecin(code);
    }

    @GetMapping
    @Operation(summary = "Obtenir des médecins par critères", description = "Recherche des médecins en fonction des critères fournis.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des médecins trouvés."),
            @ApiResponse(responseCode = "400", description = "Requête invalide.")
    })
    public Page<MedecinResponseDto> getMedecinByCriteria(
            @RequestParam(defaultValue = "0", name = "page") @Parameter(description = "Numéro de page") int page,
            @RequestParam(defaultValue = "10", name = "size") @Parameter(description = "Taille de la page") int size,
            @RequestParam(name = "id", required = false) @Parameter(description = "ID du médecin") Long id,
            @RequestParam(name = "prenom", required = false) @Parameter(description = "Prénom du médecin") String prenom,
            @RequestParam(name = "nom", required = false) @Parameter(description = "Nom du médecin") String nom,
            @RequestParam(name = "cin", required = false) @Parameter(description = "CIN du médecin") String cin,
            @RequestParam(name = "medecinCode", required = false) @Parameter(description = "Code du médecin") String medecinCode) {

        MedecinCriteria medecinCriteria = new MedecinCriteria();
        medecinCriteria.setId(id);
        medecinCriteria.setPrenom(prenom);
        medecinCriteria.setNom(nom);
        medecinCriteria.setMedecinCode(medecinCode);
        medecinCriteria.setCin(cin);
        return medecinService.findMedecinByCriteria(medecinCriteria, page, size);
    }

    @GetMapping("/download")
    public ResponseEntity<byte[]> downloadExcel()
    {
        try {
            byte[] excelFile = medecinService.generateMedecinExcel();
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
