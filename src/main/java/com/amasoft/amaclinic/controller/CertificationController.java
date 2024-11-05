package com.amasoft.amaclinic.controller;

import com.amasoft.amaclinic.Service.CertificationService;
import com.amasoft.amaclinic.criteria.CertificationCriteria;
import com.amasoft.amaclinic.dto.request.CertificationRequestDto;
import com.amasoft.amaclinic.dto.response.CertificationResponseDTO;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Optional;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.Parameter;

@RestController
@AllArgsConstructor
@RequestMapping("/certification")
public class CertificationController {

    private final CertificationService certificationService;

    @PostMapping
    @Operation(summary = "Créer une certification", description = "Ajoute une nouvelle certification.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Certification créée avec succès."),
            @ApiResponse(responseCode = "400", description = "Requête invalide.")
    })
    public CertificationRequestDto creerCertification(
            @RequestBody @Parameter(description = "Détails de la certification à ajouter") CertificationRequestDto certificationRequestDto) {
        return certificationService.creerCertification(certificationRequestDto);
    }

    @PutMapping("/{codeCertification}")
    @Operation(summary = "Mettre à jour une certification", description = "Modifie une certification existante.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Certification mise à jour avec succès."),
            @ApiResponse(responseCode = "400", description = "Requête invalide."),
            @ApiResponse(responseCode = "404", description = "Certification non trouvée.")
    })
    public CertificationRequestDto mettreAJourCertification(
            @PathVariable @Parameter(description = "Code de la certification à mettre à jour") String codeCertification,
            @RequestBody @Parameter(description = "Nouveaux détails de la certification") CertificationRequestDto certificationRequestDto) {
        return certificationService.mettreAJourCertification(codeCertification, certificationRequestDto);
    }

    @DeleteMapping("/{codeCertification}")
    @Operation(summary = "Supprimer une certification", description = "Supprime une certification existante.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Certification supprimée avec succès."),
            @ApiResponse(responseCode = "404", description = "Certification non trouvée.")
    })
    public void supprimerCertification(
            @PathVariable @Parameter(description = "Code de la certification à supprimer") String codeCertification) {
        certificationService.supprimerCertificationParCode(codeCertification);
    }

    @GetMapping("/{codeCertification}")
    @Operation(summary = "Obtenir une certification par code", description = "Récupère une certification en fonction de son code.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Certification trouvée avec succès."),
            @ApiResponse(responseCode = "404", description = "Certification non trouvée.")
    })
    public Optional<CertificationRequestDto> obtenirCertificationParCode(
            @PathVariable @Parameter(description = "Code de la certification") String codeCertification) {
        return certificationService.obtenirCertificationParCode(codeCertification);
    }

    @GetMapping
    @Operation(summary = "Trouver des certifications par critères", description = "Recherche des certifications en fonction des critères fournis.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des certifications trouvées."),
            @ApiResponse(responseCode = "400", description = "Requête invalide.")
    })
    public Page<CertificationResponseDTO> trouverCertificationParCritères(
            @RequestParam(defaultValue = "0") @Parameter(description = "Numéro de page") int page,
            @RequestParam(defaultValue = "10") @Parameter(description = "Taille de la page") int size,
            @RequestParam(required = false) @Parameter(description = "Code de la certification") String code,
            @RequestParam(required = false) @Parameter(description = "Type de la certification") String type,
            @RequestParam(required = false) @Parameter(description = "Code de la consultation associée") String consultationCode) {

        CertificationCriteria certificationCriteria = new CertificationCriteria();
        certificationCriteria.setCode(code);
        certificationCriteria.setType(type);
        certificationCriteria.setConsultationCode(consultationCode);

        return certificationService.trouverCertificationParCritères(certificationCriteria, page, size);
    }
    @GetMapping("/download")
    public ResponseEntity<byte[]> downloadExcel()
    {
        try {
            byte[] excelFile = certificationService.generateCertificationExcel();
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=certifactions.xlsx");
            headers.add(HttpHeaders.CONTENT_TYPE, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            return new ResponseEntity<>(excelFile, headers , HttpStatus.OK);
        }catch (IOException e)
        {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
