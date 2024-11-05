package com.amasoft.amaclinic.controller;

import com.amasoft.amaclinic.Service.InfermiereService;
import com.amasoft.amaclinic.criteria.InfermiereCriteria;
import com.amasoft.amaclinic.dto.request.InfermiereRequestDTO;
import com.amasoft.amaclinic.dto.response.InfermiereResponseDTO;
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
@RequestMapping("/infirmieres")
public class InfermiereController {
    private final InfermiereService infermiereService;

    @GetMapping
    @Operation(summary = "Obtenir des infirmières par critères", description = "Recherche des infirmières en fonction des critères fournis.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des infirmières trouvées."),
            @ApiResponse(responseCode = "400", description = "Requête invalide.")
    })
    public Page<InfermiereResponseDTO> getInfirmieresByCriteria(
            @RequestParam(defaultValue = "0", name = "page") @Parameter(description = "Numéro de page") int page,
            @RequestParam(defaultValue = "10", name = "size") @Parameter(description = "Taille de la page") int size,
            @RequestParam(name = "id", required = false) @Parameter(description = "ID de l'infirmière") Long id,
            @RequestParam(name = "nom", required = false) @Parameter(description = "Nom de l'infirmière") String nom,
            @RequestParam(name = "prenom", required = false) @Parameter(description = "Prénom de l'infirmière") String prenom,
            @RequestParam(name = "cin", required = false) @Parameter(description = "CIN de l'infirmière") String cin,
            @RequestParam(name = "adresse", required = false) @Parameter(description = "Adresse de l'infirmière") String adresse,
            @RequestParam(name = "telephone", required = false) @Parameter(description = "Téléphone de l'infirmière") String telephone) {

        InfermiereCriteria infermiereCriteria = new InfermiereCriteria();
        infermiereCriteria.setId(id);
        infermiereCriteria.setNom(nom);
        infermiereCriteria.setPrenom(prenom);
        infermiereCriteria.setCin(cin);
        infermiereCriteria.setAdresse(adresse);
        infermiereCriteria.setTelephone(telephone);

        return infermiereService.trouverInfirmieresParCritere(infermiereCriteria, page, size);
    }

    @PostMapping
    @Operation(summary = "Ajouter une infirmière", description = "Ajoute une nouvelle infirmière au système.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Infirmière créée avec succès."),
            @ApiResponse(responseCode = "400", description = "Requête invalide.")
    })
    public InfermiereResponseDTO save(
            @RequestBody @Parameter(description = "Détails de l'infirmière à ajouter") InfermiereRequestDTO infermiereRequestDto) {
        return infermiereService.ajouterInfermiere(infermiereRequestDto);
    }

    @PutMapping
    @Operation(summary = "Modifier une infirmière", description = "Modifie les détails d'une infirmière existante.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Infirmière modifiée avec succès."),
            @ApiResponse(responseCode = "400", description = "Requête invalide."),
            @ApiResponse(responseCode = "404", description = "Infirmière non trouvée.")
    })
    public InfermiereResponseDTO update(
            @RequestBody @Parameter(description = "Nouveaux détails de l'infirmière") InfermiereRequestDTO infermiereRequestDto) {
        return infermiereService.modifierInfermiere(infermiereRequestDto);
    }

    @DeleteMapping("/{codeInferm}")
    @Operation(summary = "Supprimer une infirmière", description = "Supprime une infirmière basée sur le code.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Infirmière supprimée avec succès."),
            @ApiResponse(responseCode = "404", description = "Infirmière non trouvée.")
    })
    public ResponseEntity<Void> deleteInfermiere(@RequestParam String codeInferm) {
        infermiereService.supprimerInfermiere(codeInferm);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/download")
    public ResponseEntity<byte[]> downloadexcel() {
        try {
            byte[] excelFile = infermiereService.generateInfermiereExcel();
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=infermiere.xlsx");
            headers.add(HttpHeaders.CONTENT_TYPE, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            return new ResponseEntity<>(excelFile, headers , HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
