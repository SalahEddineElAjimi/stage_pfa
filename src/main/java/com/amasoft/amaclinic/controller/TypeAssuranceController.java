package com.amasoft.amaclinic.controller;

import com.amasoft.amaclinic.Service.TypeAssuranceService;
import com.amasoft.amaclinic.criteria.TypeAssuranceCriteria;
import com.amasoft.amaclinic.dto.request.TypeAssuranceRequestDto;
import com.amasoft.amaclinic.dto.response.TypeAssuranceResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@AllArgsConstructor
@RequestMapping("/typeAssurance")
public class TypeAssuranceController {

    private final TypeAssuranceService typeAssuranceService;

    @PostMapping
    @Operation(summary = "Ajouter un type d'assurance", description = "Ajoute un nouveau type d'assurance au système.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Type d'assurance ajouté avec succès."),
            @ApiResponse(responseCode = "400", description = "Requête invalide.")
    })
    public TypeAssuranceResponseDto save(
            @RequestBody @Parameter(description = "Détails du type d'assurance à ajouter") TypeAssuranceRequestDto typeAssuranceRequestDto) {
        return typeAssuranceService.addTypeAssurance(typeAssuranceRequestDto);
    }

    @PutMapping
    @Operation(summary = "Modifier un type d'assurance", description = "Modifie les détails d'un type d'assurance existant.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Type d'assurance modifié avec succès."),
            @ApiResponse(responseCode = "400", description = "Requête invalide."),
            @ApiResponse(responseCode = "404", description = "Type d'assurance non trouvé.")
    })
    public TypeAssuranceResponseDto update(
            @RequestBody @Parameter(description = "Nouveaux détails du type d'assurance") TypeAssuranceRequestDto typeAssuranceRequestDto) {
        return typeAssuranceService.updateTypeAssurance(typeAssuranceRequestDto);
    }

    @DeleteMapping
    @Operation(summary = "Supprimer un type d'assurance", description = "Supprime un type d'assurance en fonction du code fourni.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Type d'assurance supprimé avec succès."),
            @ApiResponse(responseCode = "404", description = "Type d'assurance non trouvé.")
    })
    public void delete(
            @RequestParam(name = "code") @Parameter(description = "Code du type d'assurance à supprimer") String code) {
        typeAssuranceService.deleteTypeAssurance(code);
    }

    @GetMapping
    @Operation(summary = "Obtenir les types d'assurance", description = "Recherche les types d'assurance en fonction des critères fournis.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des types d'assurance trouvés."),
            @ApiResponse(responseCode = "400", description = "Requête invalide.")
    })
    public Page<TypeAssuranceResponseDto> getTypeAssuranceByCriteria(
            @RequestParam(defaultValue = "0", name = "page") @Parameter(description = "Numéro de page") int page,
            @RequestParam(defaultValue = "10", name = "size") @Parameter(description = "Taille de la page") int size,
            @RequestParam(name = "id", required = false) @Parameter(description = "ID du type d'assurance") Long id,
            @RequestParam(name = "nomAssurance", required = false) @Parameter(description = "Nom de l'assurance") String nomAssurance,
            @RequestParam(name = "typeAssurance", required = false) @Parameter(description = "Type d'assurance") String typeAssurance,
            @RequestParam(name = "codeAssuranceType", required = false) @Parameter(description = "Code du type d'assurance") String codeAssuranceType) {

        TypeAssuranceCriteria typeAssuranceCriteria = new TypeAssuranceCriteria();
        typeAssuranceCriteria.setId(id);
        typeAssuranceCriteria.setNomAssurance(nomAssurance);
        typeAssuranceCriteria.setTypeAssurance(typeAssurance);
        typeAssuranceCriteria.setCodeAssuranceType(codeAssuranceType);

        return typeAssuranceService.findTypeAssuranceByCriteria(typeAssuranceCriteria, page, size);
    }
    @GetMapping("/download")
    public ResponseEntity<byte[]> downloadExcel()
    {
        try {
            byte[] excelFile = typeAssuranceService.generateTypeAssurance();
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=typesAssurance.xlsx");
            headers.add(HttpHeaders.CONTENT_TYPE, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            return new ResponseEntity<>(excelFile, headers , HttpStatus.OK);
        }catch (IOException e)
        {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
