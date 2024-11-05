package com.amasoft.amaclinic.controller;


import com.amasoft.amaclinic.Service.ExamPatientService;
import com.amasoft.amaclinic.criteria.ExamPatientCriteria;
import com.amasoft.amaclinic.dto.request.ExamPatientRequestDto;
import com.amasoft.amaclinic.dto.response.ExamPatientResponseDto;
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
import java.time.LocalDate;

@RestController
@AllArgsConstructor
@RequestMapping("/examPatient")
public class ExamPatientConctroller {

    private final ExamPatientService examPatientService;

    @PostMapping
    @Operation(summary = "Ajouter un examen patient", description = "Ajoute un nouvel examen pour un patient.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Examen patient créé avec succès."),
            @ApiResponse(responseCode = "400", description = "Requête invalide.")
    })
    public ExamPatientResponseDto save(
            @RequestBody @Parameter(description = "Détails de l'examen patient à ajouter") ExamPatientRequestDto examPatientRequestDto) {
        return examPatientService.addExamPatient(examPatientRequestDto);
    }

    @PutMapping
    @Operation(summary = "Modifier un examen patient", description = "Modifie un examen existant pour un patient.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Examen patient mis à jour avec succès."),
            @ApiResponse(responseCode = "400", description = "Requête invalide.")
    })
    public ExamPatientResponseDto update(
            @RequestBody @Parameter(description = "Nouveaux détails de l'examen patient") ExamPatientRequestDto examPatientRequestDto) {
        return examPatientService.updateExamPatient(examPatientRequestDto);
    }

    @DeleteMapping
    @Operation(summary = "Supprimer un examen patient", description = "Supprime un examen pour un patient en fonction du code.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Examen patient supprimé avec succès."),
            @ApiResponse(responseCode = "404", description = "Examen patient non trouvé.")
    })
    public void delete(
            @RequestParam(name = "code") @Parameter(description = "Code de l'examen patient à supprimer") String code) {
        examPatientService.deleteExamPatient(code);
    }

    @GetMapping
    @Operation(summary = "Trouver des examens patients par critères", description = "Recherche des examens patients en fonction des critères fournis.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des examens patients trouvés."),
            @ApiResponse(responseCode = "400", description = "Requête invalide.")
    })
    public Page<ExamPatientResponseDto> getExamPatientByCriteria(
            @RequestParam(defaultValue = "0", name ="page") @Parameter(description = "Numéro de page") int page,
            @RequestParam(defaultValue = "10", name = "size") @Parameter(description = "Taille de la page") int size,
            @RequestParam(name = "id", required = false) @Parameter(description = "ID de l'examen patient") Long id,
            @RequestParam(name = "codeExamPatient", required = false) @Parameter(description = "Code de l'examen patient") String codeExamPatient,
            @RequestParam(name = "dateExam", required = false) @Parameter(description = "Date de l'examen") LocalDate dateExam,
            @RequestParam(name = "codePatient", required = false) @Parameter(description = "Code du patient") String codePatient) {

        ExamPatientCriteria examPatientCriteria = new ExamPatientCriteria();
        examPatientCriteria.setId(id);
        examPatientCriteria.setCodeExamPatient(codeExamPatient);
        examPatientCriteria.setDateExam(dateExam);
        examPatientCriteria.setCodePatient(codePatient);
        return examPatientService.findExamPatientByCriteria(examPatientCriteria, page, size);
    }

    @GetMapping("/download")
    public ResponseEntity<byte[]> downloadExcel()
    {
        try {
            byte[] excelFile = examPatientService.generatePatientExam();
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=examPatient.xlsx");
            headers.add(HttpHeaders.CONTENT_TYPE, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            return new ResponseEntity<>(excelFile, headers , HttpStatus.OK);
        }catch (IOException e)
        {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
