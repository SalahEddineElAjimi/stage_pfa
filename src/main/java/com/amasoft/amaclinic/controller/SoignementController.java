package com.amasoft.amaclinic.controller;

import com.amasoft.amaclinic.Service.SoignementService;
import com.amasoft.amaclinic.criteria.SoignementCriteria;
import com.amasoft.amaclinic.dto.request.SoignementRequestDTO;
import com.amasoft.amaclinic.dto.response.SoignementResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;

import java.io.IOException;


@RestController
@AllArgsConstructor
@RequestMapping("/soignements")
public class SoignementController {

    private SoignementService soignementService;

    @PostMapping
    @Operation(summary = "Ajouter un soin", description = "Ajoute un nouveau soin au système.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Soin ajouté avec succès."),
            @ApiResponse(responseCode = "400", description = "Requête invalide.")
    })
    public SoignementResponseDTO save(
            @RequestBody @Parameter(description = "Détails du soin à ajouter") SoignementRequestDTO soignementRequestDTO) {
        return soignementService.addSoignement(soignementRequestDTO);
    }

    @PutMapping
    @Operation(summary = "Modifier un soin", description = "Modifie les détails d'un soin existant.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Soin modifié avec succès."),
            @ApiResponse(responseCode = "400", description = "Requête invalide."),
            @ApiResponse(responseCode = "404", description = "Soin non trouvé.")
    })
    public SoignementResponseDTO update(
            @RequestBody @Parameter(description = "Nouveaux détails du soin") SoignementRequestDTO soignementRequestDTO) {
        return soignementService.updateSoignement(soignementRequestDTO);
    }

    @DeleteMapping
    @Operation(summary = "Supprimer un soin", description = "Supprime un soin en fonction du code fourni.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Soin supprimé avec succès."),
            @ApiResponse(responseCode = "404", description = "Soin non trouvé.")
    })
    public void delete(
            @RequestParam(name = "code") @Parameter(description = "Code du soin à supprimer") String code) {
        soignementService.deleteSoignement(code);
    }

    @GetMapping
    @Operation(summary = "Trouver des soins par critères", description = "Recherche des soins en fonction des critères fournis.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des soins trouvés."),
            @ApiResponse(responseCode = "400", description = "Requête invalide.")
    })
    public Page<SoignementResponseDTO> getSoignementByCriteria(
            @RequestParam(defaultValue = "0", name = "page") @Parameter(description = "Numéro de page") int page,
            @RequestParam(defaultValue = "10", name = "size") @Parameter(description = "Taille de la page") int size,
            @RequestParam(name = "id", required = false) @Parameter(description = "ID du soin") Long id,
            @RequestParam(name = "codeSoignement", required = false) @Parameter(description = "Code du soin") String codeSoignement,
            @RequestParam(name = "typeSoignement", required = false) @Parameter(description = "Type de soin") String typeSoignement,
            @RequestParam(name = "patientCode", required = false) @Parameter(description = "Code du patient") String patientCode,
            @RequestParam(name = "infermiereCode", required = false) @Parameter(description = "Code de l'infirmière") String infermiereCode) {

        SoignementCriteria soignementCriteria = new SoignementCriteria();
        soignementCriteria.setId(id);
        soignementCriteria.setCodeSoignement(codeSoignement);
        soignementCriteria.setTypeSoignement(typeSoignement);
        soignementCriteria.setCodePatient(patientCode);
        soignementCriteria.setCodeInfermiere(infermiereCode);

        return soignementService.findSoignementByCriteria(soignementCriteria, page, size);
    }
    @GetMapping("/download")
    public ResponseEntity<byte[]> downloadExcel()
    {
        try {
            byte[] excelFile = soignementService.generateSoignement();
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=soignements.xlsx");
            headers.add(HttpHeaders.CONTENT_TYPE, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            return new ResponseEntity<>(excelFile, headers , HttpStatus.OK);
        }catch (IOException e)
        {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
