package com.amasoft.amaclinic.controller;

import com.amasoft.amaclinic.Service.ResultatConsultService;
import com.amasoft.amaclinic.criteria.ResultatConsltCriteria;
import com.amasoft.amaclinic.dto.request.ResultatConsltRequestDTO;
import com.amasoft.amaclinic.dto.response.ResultatConsltResponseDTO;
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
@RequestMapping("/resultatConsultation")
public class ResultatConsultController {

    private final ResultatConsultService resultatConsultService;

    @PostMapping
    @Operation(summary = "Créer un résultat de consultation", description = "Crée un nouveau résultat de consultation dans le système.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Résultat de consultation créé avec succès."),
            @ApiResponse(responseCode = "400", description = "Requête invalide.")
    })
    public ResultatConsltResponseDTO save(
            @RequestBody @Parameter(description = "Détails du résultat de consultation à créer") ResultatConsltRequestDTO resultatConsltRequestDTO) {
        return resultatConsultService.addResultatConsultation(resultatConsltRequestDTO);
    }

    @PutMapping
    @Operation(summary = "Mettre à jour un résultat de consultation", description = "Met à jour les détails d'un résultat de consultation existant.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Résultat de consultation mis à jour avec succès."),
            @ApiResponse(responseCode = "400", description = "Requête invalide."),
            @ApiResponse(responseCode = "404", description = "Résultat de consultation non trouvé.")
    })
    public ResultatConsltResponseDTO update(
            @RequestBody @Parameter(description = "Nouveaux détails du résultat de consultation") ResultatConsltRequestDTO resultatConsltRequestDTO) {
        return resultatConsultService.updateResultatConsultation(resultatConsltRequestDTO);
    }

    @DeleteMapping
    @Operation(summary = "Supprimer un résultat de consultation", description = "Supprime un résultat de consultation basé sur le code.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Résultat de consultation supprimé avec succès."),
            @ApiResponse(responseCode = "404", description = "Résultat de consultation non trouvé.")
    })
    public void delete(
            @RequestParam(name = "coderesultat") @Parameter(description = "Code du résultat de consultation à supprimer") String coderesultat) {

        resultatConsultService.deleteResultatConsultation(coderesultat);
    }

    @GetMapping
    @Operation(summary = "Obtenir des résultats de consultation par critères", description = "Recherche des résultats de consultation en fonction des critères fournis.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des résultats de consultation trouvés."),
            @ApiResponse(responseCode = "400", description = "Requête invalide.")
    })
    public Page<ResultatConsltResponseDTO> getResultatByCriteria(
            @RequestParam(defaultValue = "0", name = "page") @Parameter(description = "Numéro de page") int page,
            @RequestParam(defaultValue = "10", name = "size") @Parameter(description = "Taille de la page") int size,
            @RequestParam(name = "id", required = false) @Parameter(description = "ID du résultat de consultation") Long id,
            @RequestParam(name = "codeResultatConsultation", required = false) @Parameter(description = "Code du résultat de consultation") String codeResultatConsultation,
            @RequestParam(name = "motif", required = false) @Parameter(description = "Motif du résultat de consultation") String motif,
            @RequestParam(name = "description", required = false) @Parameter(description = "Description du résultat de consultation") String description,
            @RequestParam(name = "codeConsultation", required = false) @Parameter(description = "Code de la consultation associée") String codeConsultation) {

        ResultatConsltCriteria resultatConsltCriteria = new ResultatConsltCriteria();
        resultatConsltCriteria.setId(id);
        resultatConsltCriteria.setResultaConsultation(codeResultatConsultation);
        resultatConsltCriteria.setMotif(motif);
        resultatConsltCriteria.setDescription(description);
        resultatConsltCriteria.setCodeConsultation(codeConsultation);
        return resultatConsultService.findResultatConsultationByCriteria(resultatConsltCriteria, page, size);
    }
    @GetMapping("/download")
    public ResponseEntity<byte[]> downloadExcel()
    {
        try {
            byte[] excelFile = resultatConsultService.generateResultatExcel();
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=resultatConsult.xlsx");
            headers.add(HttpHeaders.CONTENT_TYPE, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            return new ResponseEntity<>(excelFile, headers , HttpStatus.OK);
        }catch (IOException e)
        {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
