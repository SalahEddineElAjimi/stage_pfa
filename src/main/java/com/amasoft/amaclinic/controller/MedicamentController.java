package com.amasoft.amaclinic.controller;

import com.amasoft.amaclinic.Service.MedicamentService;
import com.amasoft.amaclinic.criteria.MedicamentCriteria;
import com.amasoft.amaclinic.dto.request.MedicamentRequestDTO;
import com.amasoft.amaclinic.dto.response.MedicamentResponseDTO;
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
@RequestMapping("/medicament")
public class MedicamentController {

    private final MedicamentService medicamentService;

    @PostMapping
    @Operation(summary = "Ajouter un médicament", description = "Ajoute un nouveau médicament au système.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Médicament créé avec succès."),
            @ApiResponse(responseCode = "400", description = "Requête invalide.")
    })
    public MedicamentResponseDTO save(
            @RequestBody @Parameter(description = "Détails du médicament à ajouter") MedicamentRequestDTO medicamentRequestDto) {
        return medicamentService.addMedicament(medicamentRequestDto);
    }

    @PutMapping
    @Operation(summary = "Modifier un médicament", description = "Modifie les détails d'un médicament existant.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Médicament modifié avec succès."),
            @ApiResponse(responseCode = "400", description = "Requête invalide."),
            @ApiResponse(responseCode = "404", description = "Médicament non trouvé.")
    })
    public MedicamentResponseDTO update(
            @RequestBody @Parameter(description = "Nouveaux détails du médicament") MedicamentRequestDTO medicamentRequestDto) {
        return medicamentService.updateMedicament(medicamentRequestDto);
    }

    @DeleteMapping
    @Operation(summary = "Supprimer un médicament", description = "Supprime un médicament basé sur le code.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Médicament supprimé avec succès."),
            @ApiResponse(responseCode = "404", description = "Médicament non trouvé.")
    })
    public void delete(
            @RequestParam(name = "code") @Parameter(description = "Code du médicament à supprimer") String code) {
        medicamentService.deleteMedicament(code);
    }

    @GetMapping
    @Operation(summary = "Obtenir des médicaments par critères", description = "Recherche des médicaments en fonction des critères fournis.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des médicaments trouvés."),
            @ApiResponse(responseCode = "400", description = "Requête invalide.")
    })
    public Page<MedicamentResponseDTO> getMedicamentByCriteria(
            @RequestParam(defaultValue = "0", name = "page") @Parameter(description = "Numéro de page") int page,
            @RequestParam(defaultValue = "10", name = "size") @Parameter(description = "Taille de la page") int size,
            @RequestParam(name = "id", required = false) @Parameter(description = "ID du médicament") Long id,
            @RequestParam(name = "nomMedicament", required = false) @Parameter(description = "Nom du médicament") String nomMedicament,
            @RequestParam(name = "typeMedicament", required = false) @Parameter(description = "Type du médicament") String typeMedicament,
            @RequestParam(name = "codeMedicament", required = false) @Parameter(description = "Code du médicament") String codeMedicament) {

        MedicamentCriteria medicamentCriteria = new MedicamentCriteria();
        medicamentCriteria.setId(id);
        medicamentCriteria.setNomMedicament(nomMedicament);
        medicamentCriteria.setTypeMedicament(typeMedicament);
        medicamentCriteria.setCodeMedicament(codeMedicament);
        return medicamentService.findMedicamentByCriteria(medicamentCriteria, page, size);
    }
    @GetMapping("/download")
    public ResponseEntity<byte[]> downloadExcel()
    {
        try {
            byte[] excelFile = medicamentService.generateMedecamentExcel();
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=medecaments.xlsx");
            headers.add(HttpHeaders.CONTENT_TYPE, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            return new ResponseEntity<>(excelFile, headers , HttpStatus.OK);
        }catch (IOException e)
        {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
