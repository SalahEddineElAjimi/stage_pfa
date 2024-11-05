package com.amasoft.amaclinic.Service.Impl;

import com.amasoft.amaclinic.Entity.Infermiere;
import com.amasoft.amaclinic.Entity.ParametresVitaux;
import com.amasoft.amaclinic.Entity.Patient;
import com.amasoft.amaclinic.Entity.Soignement;
import com.amasoft.amaclinic.Service.SoignementService;
import com.amasoft.amaclinic.criteria.SoignementCriteria;
import com.amasoft.amaclinic.dto.request.SoignementRequestDTO;
import com.amasoft.amaclinic.dto.response.SoignementResponseDTO;
import com.amasoft.amaclinic.exception.EntityAlreadyExisteException;
import com.amasoft.amaclinic.exception.EntityNotFoundException;
import com.amasoft.amaclinic.mapper.SoignementMapper;
import com.amasoft.amaclinic.repository.InfermiereRepository;
import com.amasoft.amaclinic.repository.PatientRepository;
import com.amasoft.amaclinic.repository.SoignementRepository;
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
public class SoignementServiceImp implements SoignementService {
    private SoignementRepository soignementRepository;
    private PatientRepository patientRepository;
    private InfermiereRepository infermiereRepository;
    private SoignementMapper soignementMapper;

    @Override
    public SoignementResponseDTO addSoignement(SoignementRequestDTO soignementRequestDTO) {
        String generatedCodeSoignement = "SOIN" + UUID.randomUUID().toString().replace("-", "").substring(0, 8);
        Soignement soignementToSave = soignementMapper.dtoToModel(soignementRequestDTO);
        soignementToSave.setCodeSoignement(generatedCodeSoignement);

        Optional<Patient> patient = patientRepository.findPatientByCodePatient(soignementRequestDTO.getCodePatient());
        if (patient.isEmpty()) {
            throw new EntityNotFoundException("Aucun patient trouvé avec le code : " + soignementRequestDTO.getCodePatient());
        }
        soignementToSave.setPatient(patient.get());

        Optional<Infermiere> infermiere = infermiereRepository.findInfermiereByCodeInferm(soignementRequestDTO.getCodeInfermiere());
        if (infermiere.isEmpty()) {
            throw new EntityNotFoundException("Aucun infermiere trouvé avec le code : " + soignementRequestDTO.getCodeInfermiere());
        }
        soignementToSave.setInfermiere(infermiere.get());

        Optional<Soignement> existingSoin = soignementRepository.findSoignementByCodeSoignement(soignementRequestDTO.getCodeSoignement());
        if (existingSoin.isPresent()) {
            throw new EntityAlreadyExisteException("soin already exists with id: " + soignementRequestDTO.getId());
        }
        Soignement savedSoignement = soignementRepository.save(soignementToSave);
        return soignementMapper.modelToDto(savedSoignement);
    }

    @Override
    public SoignementResponseDTO updateSoignement(SoignementRequestDTO soignementRequestDTO) {
        Optional<Soignement> existingSoignement = soignementRepository.findSoignementByCodeSoignement(soignementRequestDTO.getCodeSoignement());
        if (existingSoignement.isEmpty()) {
            throw new EntityNotFoundException("Soignement non trouvé avec le code : " + soignementRequestDTO.getCodeSoignement());
        }

        // Conversion du DTO en modèle
        Soignement soignementToUpdate = soignementMapper.dtoToModel(soignementRequestDTO);

        // Mise à jour des champs critiques de l'entité
        soignementToUpdate.setId(existingSoignement.get().getId());
        soignementToUpdate.setCodeSoignement(existingSoignement.get().getCodeSoignement());

        // Mise à jour des relations avec Patient et Infermiere
        Optional<Patient> patient = patientRepository.findPatientByCodePatient(soignementRequestDTO.getCodePatient());
        if (patient.isEmpty()) {
            throw new EntityNotFoundException("Patient non trouvé avec le code : " + soignementRequestDTO.getCodePatient());
        }
        soignementToUpdate.setPatient(patient.get());

        Optional<Infermiere> infermiere = infermiereRepository.findInfermiereByCodeInferm(soignementRequestDTO.getCodeInfermiere());
        if (infermiere.isEmpty()) {
            throw new EntityNotFoundException("Infermiere non trouvée avec le code : " + soignementRequestDTO.getCodeInfermiere());
        }
        soignementToUpdate.setInfermiere(infermiere.get());

        // Sauvegarde de l'entité mise à jour
        Soignement updatedSoignement = soignementRepository.save(soignementToUpdate);

        // Conversion du modèle mis à jour en DTO de réponse et retour
        return soignementMapper.modelToDto(updatedSoignement);
    }

    @Override
    public void deleteSoignement(String codeSoignement) {
        Optional<Soignement> soignement = soignementRepository.findSoignementByCodeSoignement(codeSoignement);
        if(soignement.isEmpty()) {
            throw new EntityAlreadyExisteException("soignement non trouve avec le code:" + codeSoignement);
        }
        soignementRepository.deleteByCodeSoignement(codeSoignement);
    }

    @Override
    public Page<SoignementResponseDTO> findSoignementByCriteria(SoignementCriteria soignementCriteria, int page, int size) {
        PageRequest pageable = PageRequest.of(page, size);

        Page<Soignement> soignementPage = soignementRepository.findAll((root, query, criteriaBuilder) -> {
            List<Predicate> predicateList = new ArrayList<>();

            // Ajout de critères dynamiques
            if (soignementCriteria.getId() != null) {
                predicateList.add(criteriaBuilder.equal(root.get("id"), soignementCriteria.getId()));
            }
            if (soignementCriteria.getTypeSoignement() != null) {
                predicateList.add(criteriaBuilder.equal(root.get("typeSoignement"), soignementCriteria.getTypeSoignement()));
            }
            if (soignementCriteria.getCodeSoignement() != null) {
                predicateList.add(criteriaBuilder.equal(root.get("codeSoignement"), soignementCriteria.getCodeSoignement()));
            }
            if (soignementCriteria.getCodePatient() != null) {
                predicateList.add(criteriaBuilder.equal(root.get("patient").get("codePatient"), soignementCriteria.getCodePatient()));
            }
            if (soignementCriteria.getCodeInfermiere() != null) {
                predicateList.add(criteriaBuilder.equal(root.get("infermiere").get("codeInfermiere"), soignementCriteria.getCodeSoignement()));
            }

            // Combinaison des prédicats avec un AND logique
            return criteriaBuilder.and(predicateList.toArray(new Predicate[0]));

        }, pageable);

        // Conversion de la page d'entités en page de DTOs
        return soignementMapper.modelToDtos(soignementPage);
    }

    @Override
    public byte[] generateSoignement() throws IOException {
        List<Soignement> soignements = soignementRepository.findAll();
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Soignements");
        Row headerRow = sheet.createRow(0);
        String[] headers = {
                "ID",
                "Code Soignement",
                "Type Soignement",
                "Description",
                "Code Patient",
                "Code Infermiere"
        };

        // Création de l'en-tête
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }

        int rowNum = 1;
        // Remplissage des données
        for (Soignement soignement : soignements) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(soignement.getId());
            row.createCell(1).setCellValue(soignement.getCodeSoignement());
            row.createCell(2).setCellValue(soignement.getTypeSoignement());
            row.createCell(3).setCellValue(soignement.getDescription());
            row.createCell(4).setCellValue(soignement.getPatient().getCodePatient());
            row.createCell(5).setCellValue(soignement.getInfermiere().getCodeInferm());
        }

        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            workbook.write(byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        }
    }
}
