package com.amasoft.amaclinic.controller;

import com.amasoft.amaclinic.Service.OrdonnanceService;
import com.amasoft.amaclinic.criteria.OrdonnanceCriteria;
import com.amasoft.amaclinic.dto.request.OrdonnanceRequestDTO;
import com.amasoft.amaclinic.dto.response.OrdonnanceResponseDTO;
import lombok.AllArgsConstructor;
import net.sf.jasperreports.engine.JRException;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;


@RestController
@AllArgsConstructor
@RequestMapping("/ordonnance")
public class OrdonnanceController {

    private final OrdonnanceService ordonnanceService;

    @PostMapping
    @Operation(summary = "Ajouter une ordonnance", description = "Ajoute une nouvelle ordonnance au système.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Ordonnance créée avec succès."),
            @ApiResponse(responseCode = "400", description = "Requête invalide.")
    })
    public OrdonnanceResponseDTO save(
            @RequestBody @Parameter(description = "Détails de l'ordonnance à ajouter") OrdonnanceRequestDTO ordonnanceRequestDto) {
        return ordonnanceService.addOrdonnance(ordonnanceRequestDto);
    }

    @GetMapping("/{code}/report")
    public ResponseEntity<byte[]>generateOrdonnanceReport(@PathVariable String code) throws JRException, IOException{
        return ordonnanceService.generateOrdonnanceReport(code);
    }

    @PutMapping
    @Operation(summary = "Modifier une ordonnance", description = "Modifie les détails d'une ordonnance existante.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ordonnance modifiée avec succès."),
            @ApiResponse(responseCode = "400", description = "Requête invalide."),
            @ApiResponse(responseCode = "404", description = "Ordonnance non trouvée.")
    })
    public OrdonnanceResponseDTO update(
            @RequestBody @Parameter(description = "Nouveaux détails de l'ordonnance") OrdonnanceRequestDTO ordonnanceRequestDto) {
        return ordonnanceService.updateOrdonnance(ordonnanceRequestDto);
    }

    @DeleteMapping
    @Operation(summary = "Supprimer une ordonnance", description = "Supprime une ordonnance basée sur le code.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Ordonnance supprimée avec succès."),
            @ApiResponse(responseCode = "404", description = "Ordonnance non trouvée.")
    })
    public void delete(
            @RequestParam(name = "code") @Parameter(description = "Code de l'ordonnance à supprimer") String code) {
        ordonnanceService.deleteOrdonnance(code);
    }

    @GetMapping
    @Operation(summary = "Obtenir des ordonnances par critères", description = "Recherche des ordonnances en fonction des critères fournis.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des ordonnances trouvées."),
            @ApiResponse(responseCode = "400", description = "Requête invalide.")
    })
    public Page<OrdonnanceResponseDTO> getOrdonnanceByCriteria(
            @RequestParam(defaultValue = "0", name = "page") @Parameter(description = "Numéro de page") int page,
            @RequestParam(defaultValue = "10", name = "size") @Parameter(description = "Taille de la page") int size,
            @RequestParam(name = "id", required = false) @Parameter(description = "ID de l'ordonnance") Long id,
            @RequestParam(name = "dateOrdonnance", required = false) @Parameter(description = "Date de l'ordonnance") LocalDate dateOrdonnance,
            @RequestParam(name = "detailsOrdonnance", required = false) @Parameter(description = "Détails de l'ordonnance") String detailsOrdonnance,
            @RequestParam(name = "codeOrdonnance", required = false) @Parameter(description = "Code de l'ordonnance") String codeOrdonnance) {

        OrdonnanceCriteria ordonnanceCriteria = new OrdonnanceCriteria();
        ordonnanceCriteria.setId(id);
        ordonnanceCriteria.setDateOrdonnance(dateOrdonnance);
        ordonnanceCriteria.setDetailsOrdonnance(detailsOrdonnance);
        ordonnanceCriteria.setCodeOrdonnance(codeOrdonnance);
        return ordonnanceService.findOrdonnanceByCriteria(ordonnanceCriteria, page, size);
    }

    @GetMapping("/download")
    public ResponseEntity<byte[]> downloadExcel()
    {
        try {
            byte[] excelFile = ordonnanceService.generateOrdonnanceEXCEL();
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=ordonnances.xlsx");
            headers.add(HttpHeaders.CONTENT_TYPE, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            return new ResponseEntity<>(excelFile, headers , HttpStatus.OK);
        }catch (IOException e)
        {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
