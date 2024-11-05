package com.amasoft.amaclinic.controller;

import com.amasoft.amaclinic.Service.PaimentService;
import com.amasoft.amaclinic.criteria.PaimentCriteria;
import com.amasoft.amaclinic.dto.request.PaimentRequestDto;
import com.amasoft.amaclinic.dto.response.PaimentResponseDto;
import com.amasoft.amaclinic.exception.EntityNotFoundException;
import lombok.AllArgsConstructor;
import net.sf.jasperreports.engine.JRException;
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
@RequestMapping("/paiment")
public class PaimentController {

    private final PaimentService paimentService;

    @PostMapping
    @Operation(summary = "Créer un paiement", description = "Crée un nouveau paiement.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Paiement créé avec succès."),
            @ApiResponse(responseCode = "400", description = "Requête invalide.")
    })
    public PaimentResponseDto create(
            @RequestBody @Parameter(description = "Détails du paiement à créer") PaimentRequestDto paimentRequestDto) {
        return paimentService.creerPaiment(paimentRequestDto);
    }

    @PutMapping("/{codePaiment}")
    @Operation(summary = "Mettre à jour un paiement", description = "Met à jour les détails d'un paiement existant.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Paiement mis à jour avec succès."),
            @ApiResponse(responseCode = "400", description = "Requête invalide."),
            @ApiResponse(responseCode = "404", description = "Paiement non trouvé.")
    })
    public PaimentResponseDto update(
            @PathVariable @Parameter(description = "Code du paiement à mettre à jour") String codePaiment,
            @RequestBody @Parameter(description = "Nouveaux détails du paiement") PaimentRequestDto paimentRequestDto) {
        return paimentService.mettreAJourPaiment(codePaiment, paimentRequestDto);
    }

    @DeleteMapping("/{codePaiment}")
    @Operation(summary = "Supprimer un paiement", description = "Supprime un paiement basé sur le code.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Paiement supprimé avec succès."),
            @ApiResponse(responseCode = "404", description = "Paiement non trouvé.")
    })
    public void delete(
            @PathVariable @Parameter(description = "Code du paiement à supprimer") String codePaiment) {
        paimentService.supprimerPaimentParCode(codePaiment);
    }

    @GetMapping("/{codePaiment}")
    @Operation(summary = "Obtenir un paiement par code", description = "Recherche un paiement en fonction du code fourni.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Paiement trouvé avec succès."),
            @ApiResponse(responseCode = "404", description = "Paiement non trouvé.")
    })
    public PaimentResponseDto getPaimentByCode(
            @PathVariable @Parameter(description = "Code du paiement à rechercher") String codePaiment) {
        return paimentService.obtenirPaimentParCode(codePaiment)
                .orElseThrow(() -> new RuntimeException("Paiement non trouvé avec le code : " + codePaiment));
    }

    @GetMapping
    @Operation(summary = "Obtenir des paiements par critères", description = "Recherche des paiements en fonction des critères fournis.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des paiements trouvés."),
            @ApiResponse(responseCode = "400", description = "Requête invalide.")
    })
    public Page<PaimentResponseDto> getPaimentsByCriteria(
            @RequestParam(defaultValue = "0", name = "page") @Parameter(description = "Numéro de page") int page,
            @RequestParam(defaultValue = "10", name = "size") @Parameter(description = "Taille de la page") int size,
            @RequestParam(name = "codePaiment", required = false) @Parameter(description = "Code du paiement") String codePaiment,
            @RequestParam(name = "datePaimentFrom", required = false) @Parameter(description = "Date de début du paiement") LocalDateTime datePaimentFrom,
            @RequestParam(name = "datePaimentTo", required = false) @Parameter(description = "Date de fin du paiement") LocalDateTime datePaimentTo,
            @RequestParam(name = "montantMin", required = false) @Parameter(description = "Montant minimum") Integer montantMin,
            @RequestParam(name = "montantMax", required = false) @Parameter(description = "Montant maximum") Integer montantMax,
            @RequestParam(name = "typePaiment", required = false) @Parameter(description = "Type de paiement") String typePaiment,
            @RequestParam(name = "codeConsultation", required = false) @Parameter(description = "Code de la consultation associée") String codeConsultation) {

        PaimentCriteria paimentCriteria = PaimentCriteria.builder()
                .codePaiment(codePaiment)
                .datePaimentFrom(datePaimentFrom)
                .datePaimentTo(datePaimentTo)
                .montantMin(montantMin)
                .montantMax(montantMax)
                .typePaiment(typePaiment)
                .codeConsultation(codeConsultation)
                .build();

        return paimentService.trouverPaimentParCritères(paimentCriteria, page, size);
    }

    @GetMapping("/generateReceipt/{codePaiment}")
    public ResponseEntity<byte[]> generateReceipt(@PathVariable("codePaiment") String codePaiment) {
        try {
            return paimentService.generatePaymentReceiptReport(codePaiment);
        } catch (JRException | IOException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(null);
        }
    }

    @GetMapping("/download")
    public ResponseEntity<byte[]> downloadExcel()
    {
        try {
            byte[] excelFile = paimentService.generatePaiment();
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=paiments.xlsx");
            headers.add(HttpHeaders.CONTENT_TYPE, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            return new ResponseEntity<>(excelFile, headers , HttpStatus.OK);
        }catch (IOException e)
        {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
