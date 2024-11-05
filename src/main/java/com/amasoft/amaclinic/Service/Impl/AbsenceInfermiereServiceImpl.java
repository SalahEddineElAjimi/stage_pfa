package com.amasoft.amaclinic.Service.Impl;

import com.amasoft.amaclinic.Entity.AbsenceInfermiere;
import com.amasoft.amaclinic.Entity.Infermiere;
import com.amasoft.amaclinic.Service.AbsenceInfermiereService;
import com.amasoft.amaclinic.Service.InfermiereService; // Assurez-vous d'importer le service ou repository
import com.amasoft.amaclinic.criteria.AbsenceInfermiereCriteria;
import com.amasoft.amaclinic.dto.request.AbsenceInfermiereRequestDTO;
import com.amasoft.amaclinic.dto.response.AbsenceInfermiereResponseDTO;
import com.amasoft.amaclinic.exception.EntityNotFoundException;
import com.amasoft.amaclinic.mapper.AbscenceInfermiereMapper;
import com.amasoft.amaclinic.repository.AbsenceInfermiereRepository;
import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
@Transactional
public class AbsenceInfermiereServiceImpl implements AbsenceInfermiereService {
    private final AbsenceInfermiereRepository absenceRepository;
    private final AbscenceInfermiereMapper absenceMapper;
    private final InfermiereService infermiereService; // Ajouter le service ou repository pour Infermiere

    @Override
    public AbsenceInfermiereResponseDTO ajouterAbsence(AbsenceInfermiereRequestDTO absenceRequestDTO) {
        // Recherche de l'infirmière par codeInferm
        Optional<Infermiere> infermiereOpt = infermiereService.findByCodeInferm(absenceRequestDTO.getCodeInferm());
        if (infermiereOpt.isEmpty()) {
            throw new EntityNotFoundException("Infirmière non trouvée avec le Code : " + absenceRequestDTO.getCodeInferm());
        }

        String generatedCode = "ABSENCE" + UUID.randomUUID().toString().replace("-", "").substring(0, 8);
        absenceRequestDTO.setCodeAbscence(generatedCode);

        AbsenceInfermiere absenceToSave = absenceMapper.dtoToModel(absenceRequestDTO);
        absenceToSave.setInfermiere(infermiereOpt.get()); // Associer l'infirmière à l'absence

        if (absenceToSave.getDateFin().isBefore(absenceToSave.getDateDebut())) {
            throw new IllegalArgumentException("La date de fin doit être après la date de début.");
        }

        AbsenceInfermiere savedAbsence = absenceRepository.save(absenceToSave);
        return absenceMapper.modelToDto(savedAbsence);
    }


    @Override
    public AbsenceInfermiereResponseDTO modifierAbsence(AbsenceInfermiereRequestDTO absenceRequestDTO) {
        Optional<AbsenceInfermiere> existingAbsenceOpt = absenceRepository.findByCodeAbscence(absenceRequestDTO.getCodeAbscence());
        if (existingAbsenceOpt.isEmpty()) {
            throw new EntityNotFoundException("Absence non trouvée avec le Code : " + absenceRequestDTO.getCodeAbscence());
        }

        AbsenceInfermiere existingAbsence = existingAbsenceOpt.get();
        AbsenceInfermiere absenceToUpdate = absenceMapper.dtoToModel(absenceRequestDTO);

        // Recherche de l'infirmière par codeInferm
        Optional<Infermiere> infermiereOpt = infermiereService.findByCodeInferm(absenceRequestDTO.getCodeInferm());
        if (infermiereOpt.isEmpty()) {
            throw new EntityNotFoundException("Infirmière non trouvée avec le Code : " + absenceRequestDTO.getCodeInferm());
        }
        absenceToUpdate.setInfermiere(infermiereOpt.get()); // Associer l'infirmière à l'absence

        // Assurez-vous que l'ID est correctement défini
        absenceToUpdate.setIdAbsence(existingAbsence.getIdAbsence());

        // Log pour vérifier les valeurs
        System.out.println("Absence to update: " + absenceToUpdate);

        AbsenceInfermiere updatedAbsence = absenceRepository.save(absenceToUpdate);
        return absenceMapper.modelToDto(updatedAbsence);
    }

    @Override
    public void supprimerAbsence(String codeAbscence) {
        Optional<AbsenceInfermiere> absenceOpt = absenceRepository.findByCodeAbscence(codeAbscence);
        if (absenceOpt.isEmpty()) {
            throw new EntityNotFoundException("Absence non trouvée avec le code : " + codeAbscence);
        }
        absenceRepository.deleteByCodeAbscence(codeAbscence);
    }

    @Override
    public Page<AbsenceInfermiereResponseDTO> trouverAbsencesParCritere(AbsenceInfermiereCriteria criteria, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        return absenceRepository.findAll((root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (criteria.getIdAbsence() != null) {
                predicates.add(criteriaBuilder.equal(root.get("idAbsence"), criteria.getIdAbsence()));
            }
            if (criteria.getCodeAbscence() != null) {
                predicates.add(criteriaBuilder.equal(root.get("codeAbscence"), criteria.getCodeAbscence()));
            }
            if (criteria.getDateDebut() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("dateDebut"), criteria.getDateDebut()));
            }
            if (criteria.getDateFin() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("dateFin"), criteria.getDateFin()));
            }
            if (criteria.getIdInfermiere() != null) {
                predicates.add(criteriaBuilder.equal(root.get("infermiere").get("idInfermiere"), criteria.getIdInfermiere()));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        }, pageable).map(absenceMapper::modelToDto);
    }

    @Override
    public byte[] generateAbsInferExcel() throws IOException {
        List<AbsenceInfermiere> absences = absenceRepository.findAll();
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("AbsencesInfermieres");
        Row headerRow = sheet.createRow(0);
        String[] headers = {"ID Absence", "Code Absence", "Date Début", "Date Fin", "Code Infirmière"};

        // Création de l'en-tête
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }

        int rowNum = 1;
        // Remplissage des données
        for (AbsenceInfermiere absence : absences) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(absence.getIdAbsence());
            row.createCell(1).setCellValue(absence.getCodeAbscence());
            row.createCell(2).setCellValue(absence.getDateDebut().toString());
            row.createCell(3).setCellValue(absence.getDateFin().toString());
            row.createCell(4).setCellValue(absence.getInfermiere().getCodeInferm());
        }

        // Écriture du fichier Excel en mémoire et retour en tableau d'octets
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            workbook.write(byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        }
    }
}
