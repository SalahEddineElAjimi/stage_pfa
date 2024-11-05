package com.amasoft.amaclinic.controller;

import com.amasoft.amaclinic.Service.SecretaireService;
import com.amasoft.amaclinic.criteria.secretaireCriteria;
import com.amasoft.amaclinic.dto.request.secretaireRequestDto;
import com.amasoft.amaclinic.dto.response.responseSecretaire;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/secretaire")
@AllArgsConstructor
public class SecretaireController {

    @Autowired
    private SecretaireService secretaireService;

    @GetMapping
    @Operation(summary = "Trouver des secrétaires par critères", description = "Recherche des secrétaires en fonction des critères fournis.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des secrétaires trouvés."),
            @ApiResponse(responseCode = "400", description = "Requête invalide.")
    })
    public Page<responseSecretaire> trouverSecretaireByCriteria(
            @RequestParam(defaultValue = "0", name = "page") @Parameter(description = "Numéro de page") int page,
            @RequestParam(defaultValue = "10", name = "size") @Parameter(description = "Taille de la page") int size,
            @RequestParam(name = "id", required = false) @Parameter(description = "ID du secrétaire") Long id,
            @RequestParam(name = "nom", required = false) @Parameter(description = "Nom du secrétaire") String nom,
            @RequestParam(name = "prenom", required = false) @Parameter(description = "Prénom du secrétaire") String prenom,
            @RequestParam(name = "CodeSecretaire", required = false) @Parameter(description = "Code du secrétaire") String CodeSecretaire) {

        secretaireCriteria secretaireCriteria = new secretaireCriteria();
        secretaireCriteria.setId(id);
        secretaireCriteria.setCodeSecretaire(CodeSecretaire);
        secretaireCriteria.setNom(nom);
        secretaireCriteria.setPrenom(prenom);
        return secretaireService.trouverSecretaireByCriteria(secretaireCriteria, page, size);
    }

    @PostMapping
    @Operation(summary = "Ajouter un secrétaire", description = "Ajoute un nouveau secrétaire au système.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Secrétaire ajouté avec succès."),
            @ApiResponse(responseCode = "400", description = "Requête invalide.")
    })
    public responseSecretaire save(
            @RequestBody @Parameter(description = "Détails du secrétaire à ajouter") secretaireRequestDto secretaireRequestDto) {
        return secretaireService.addSecretaire(secretaireRequestDto);
    }

    @PutMapping
    @Operation(summary = "Modifier un secrétaire", description = "Modifie les détails d'un secrétaire existant.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Secrétaire modifié avec succès."),
            @ApiResponse(responseCode = "400", description = "Requête invalide."),
            @ApiResponse(responseCode = "404", description = "Secrétaire non trouvé.")
    })
    public responseSecretaire update(
            @RequestBody @Parameter(description = "Nouveaux détails du secrétaire") secretaireRequestDto secretaireRequestDto) {
        return secretaireService.updateSecretaire(secretaireRequestDto);
    }

    @DeleteMapping
    @Operation(summary = "Supprimer un secrétaire", description = "Supprime un secrétaire en fonction du code fourni.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Secrétaire supprimé avec succès."),
            @ApiResponse(responseCode = "404", description = "Secrétaire non trouvé.")
    })
    public void delete(
            @RequestParam(name = "CodeSecretaire") @Parameter(description = "Code du secrétaire à supprimer") String CodeSecretaire) {
        secretaireService.deleteSecretaire(CodeSecretaire);
    }


    @GetMapping("/download")
    public ResponseEntity<byte[]> downloadExcel()
    {
        try {
            byte[] excelFile = secretaireService.generateSercretaireExcel();
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=secretaires.xlsx");
            headers.add(HttpHeaders.CONTENT_TYPE, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            return new ResponseEntity<>(excelFile, headers , HttpStatus.OK);
        }catch (IOException e)
        {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }




}
