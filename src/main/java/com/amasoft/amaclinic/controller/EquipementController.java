package com.amasoft.amaclinic.controller;

import com.amasoft.amaclinic.dto.request.EquipementRequestDTO;
import com.amasoft.amaclinic.dto.response.EquipementResponseDTO;
import com.amasoft.amaclinic.criteria.EquipementCriteria;
import com.amasoft.amaclinic.Service.EquipementService;
import com.amasoft.amaclinic.exception.EntityNotFoundException;
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
@RequestMapping("/equipements")
@AllArgsConstructor
public class EquipementController {

    private final EquipementService equipementService;

    @PostMapping
    @Operation(summary = "Ajouter un équipement", description = "Ajoute un nouvel équipement au système.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Équipement créé avec succès."),
            @ApiResponse(responseCode = "400", description = "Requête invalide.")
    })
    public ResponseEntity<EquipementResponseDTO> ajouterEquipement(
            @RequestBody @Parameter(description = "Détails de l'équipement à ajouter") EquipementRequestDTO equipementRequestDTO) {
        try {
            EquipementResponseDTO equipementResponseDTO = equipementService.ajouterEquipement(equipementRequestDTO);
            return new ResponseEntity<>(equipementResponseDTO, HttpStatus.CREATED);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping
    @Operation(summary = "Modifier un équipement", description = "Modifie les détails d'un équipement existant.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Équipement modifié avec succès."),
            @ApiResponse(responseCode = "400", description = "Requête invalide."),
            @ApiResponse(responseCode = "404", description = "Équipement non trouvé.")
    })
    public ResponseEntity<EquipementResponseDTO> modifierEquipement(
            @RequestBody @Parameter(description = "Nouveaux détails de l'équipement") EquipementRequestDTO equipementRequestDTO) {
        try {
            EquipementResponseDTO equipementResponseDTO = equipementService.modifierEquipement(equipementRequestDTO);
            return new ResponseEntity<>(equipementResponseDTO, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    @DeleteMapping("/{codeEquipement}")
    @Operation(summary = "Supprimer un équipement", description = "Supprime un équipement basé sur l'ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Équipement supprimé avec succès."),
            @ApiResponse(responseCode = "404", description = "Équipement non trouvé.")
    })
    public ResponseEntity<Void> supprimerEquipement(
            @PathVariable("codeEquipement") @Parameter(description = "ID de l'équipement à supprimer") String codeEquipement) {
        try {
            equipementService.supprimerEquipement(codeEquipement);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }




    @GetMapping
    @Operation(summary = "Trouver des équipements par critères", description = "Recherche des équipements en fonction des critères fournis.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des équipements trouvés."),
            @ApiResponse(responseCode = "400", description = "Requête invalide.")
    })
    public ResponseEntity<Page<EquipementResponseDTO>> trouverEquipementsParCritere(
            @RequestParam(value = "codeEquipement", required = false) @Parameter(description = "Code de l'équipement") String codeEquipement,
            @RequestParam(value = "nomEquipement", required = false) @Parameter(description = "Nom de l'équipement") String nomEquipement,
            @RequestParam(value = "salleId", required = false) @Parameter(description = "ID de la salle") Long salleId,
            @RequestParam(value = "page", defaultValue = "0") @Parameter(description = "Numéro de page") int page,
            @RequestParam(value = "size", defaultValue = "10") @Parameter(description = "Taille de la page") int size) {

        EquipementCriteria equipementCriteria = new EquipementCriteria();
        equipementCriteria.setCodeEquipement(codeEquipement);
        equipementCriteria.setNomEquipement(nomEquipement);
        equipementCriteria.setSalleId(salleId);

        Page<EquipementResponseDTO> equipementPage = equipementService.trouverEquipementsParCritere(equipementCriteria, page, size);
        return new ResponseEntity<>(equipementPage, HttpStatus.OK);
    }

    @GetMapping("/download")
    public ResponseEntity<byte[]> downloadExcel()
    {
        try {
            byte[] excelFile = equipementService.generateEquipementExcel();
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=equipements.xlsx");
            headers.add(HttpHeaders.CONTENT_TYPE, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            return new ResponseEntity<>(excelFile, headers , HttpStatus.OK);
        }catch (IOException e)
        {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
