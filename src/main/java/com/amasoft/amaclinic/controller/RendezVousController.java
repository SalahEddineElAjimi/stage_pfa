package com.amasoft.amaclinic.controller;


import com.amasoft.amaclinic.Service.RendezVousService;
import com.amasoft.amaclinic.criteria.RendezVousCriteria;
import com.amasoft.amaclinic.dto.request.RendezVousRequestDTO;
import com.amasoft.amaclinic.dto.response.RendezVousResponseDTO;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;


@RestController
@AllArgsConstructor
@RequestMapping("/rendezVous")
public class RendezVousController {

    private final RendezVousService rendezVousService;

    @PostMapping
    @Operation(summary = "Créer un rendez-vous", description = "Crée un nouveau rendez-vous dans le système.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Rendez-vous créé avec succès."),
            @ApiResponse(responseCode = "400", description = "Requête invalide.")
    })
    public RendezVousResponseDTO save(
            @RequestBody @Parameter(description = "Détails du rendez-vous à créer") RendezVousRequestDTO rendezVousRequestDTO) {
        return rendezVousService.addRendezVous(rendezVousRequestDTO);
    }

    @PutMapping
    @Operation(summary = "Mettre à jour un rendez-vous", description = "Met à jour les détails d'un rendez-vous existant.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rendez-vous mis à jour avec succès."),
            @ApiResponse(responseCode = "400", description = "Requête invalide."),
            @ApiResponse(responseCode = "404", description = "Rendez-vous non trouvé.")
    })
    public RendezVousResponseDTO update(
            @RequestBody @Parameter(description = "Nouveaux détails du rendez-vous") RendezVousRequestDTO rendezVousRequestDTO) {
        return rendezVousService.updateRendezVous(rendezVousRequestDTO);
    }

    @DeleteMapping
    @Operation(summary = "Supprimer un rendez-vous", description = "Supprime un rendez-vous basé sur le code.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Rendez-vous supprimé avec succès."),
            @ApiResponse(responseCode = "404", description = "Rendez-vous non trouvé.")
    })
    public void delete(
            @RequestParam(name = "code") @Parameter(description = "Code du rendez-vous à supprimer") String code) {
        rendezVousService.deleteRendezVous(code);
    }

    @GetMapping
    @Operation(summary = "Obtenir des rendez-vous par critères", description = "Recherche des rendez-vous en fonction des critères fournis.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des rendez-vous trouvés."),
            @ApiResponse(responseCode = "400", description = "Requête invalide.")
    })
    public Page<RendezVousResponseDTO> getParametreByCriteria(
            @RequestParam(defaultValue = "0", name = "page") @Parameter(description = "Numéro de page") int page,
            @RequestParam(defaultValue = "10", name = "size") @Parameter(description = "Taille de la page") int size,
            @RequestParam(name = "id", required = false) @Parameter(description = "ID du rendez-vous") Long id,
            @RequestParam(name = "typeRDV", required = false) @Parameter(description = "Type du rendez-vous") String typeRDV,
            @RequestParam(name = "status", required = false) @Parameter(description = "Statut du rendez-vous") String status,
            @RequestParam(name = "dateDebutRDV", required = false) @Parameter(description = "Date de début du rendez-vous") LocalDateTime dateDebutRDV,
            @RequestParam(name = "dateFinRDV", required = false) @Parameter(description = "Date de fin du rendez-vous") LocalDateTime dateFinRDV,
            @RequestParam(name = "codeRendezVous", required = false) @Parameter(description = "Code du rendez-vous") String codeRendezVous) {

        RendezVousCriteria rendezVousCriteria = new RendezVousCriteria();
        rendezVousCriteria.setId(id);
        rendezVousCriteria.setTypeRDV(typeRDV);
        rendezVousCriteria.setStatus(status);
        rendezVousCriteria.setDateDebutRDV(dateDebutRDV);
        rendezVousCriteria.setDateFinRDV(dateFinRDV);
        rendezVousCriteria.setCodeRendezVous(codeRendezVous);
        return rendezVousService.findRendezVousByCriteria(rendezVousCriteria, page, size);
    }

    @GetMapping("/download")
    public ResponseEntity<byte[]> downloadExcel()
    {
        try {
            byte[] excelFile = rendezVousService.generateRdvExcel();
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
