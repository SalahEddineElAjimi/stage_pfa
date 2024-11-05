package com.amasoft.amaclinic.Service.Impl;


import com.amasoft.amaclinic.Entity.AssurancePatient;
import com.amasoft.amaclinic.Entity.Patient;
import com.amasoft.amaclinic.Entity.TypeAssurance;
import com.amasoft.amaclinic.Service.AssurancePatientService;
import com.amasoft.amaclinic.criteria.AssurancePatientCriteria;

import com.amasoft.amaclinic.dto.request.AssurancePatientRequestDto;
import com.amasoft.amaclinic.dto.response.AssurancePatientResponseDto;
import com.amasoft.amaclinic.exception.EntityAlreadyExisteException;
import com.amasoft.amaclinic.exception.EntityNotFoundException;

import com.amasoft.amaclinic.mapper.AssurancePatientMapper;
import com.amasoft.amaclinic.repository.AssurancePatientRepository;
import com.amasoft.amaclinic.repository.PatientRepository;
import com.amasoft.amaclinic.repository.TypeAssuranceRepository;
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
public class AssurancePatientServiceImpl implements AssurancePatientService {
    private AssurancePatientRepository assurancePatientRepository;
    private PatientRepository patientRepo;
    private TypeAssuranceRepository typeAssuranceRepo;
    private AssurancePatientMapper assurancePatientMapper;

    @Override
    public AssurancePatientResponseDto addAssurancePatient(AssurancePatientRequestDto assurancePatientRequestDto) {
        String generatedCodeAssurancepatient = "ASSP" + UUID.randomUUID().toString().replace("-", "").substring(0, 6);
        AssurancePatient assuranceToSave = assurancePatientMapper.dtoToModel(assurancePatientRequestDto);
        assuranceToSave.setCodeAssurance(generatedCodeAssurancepatient);

        Optional<Patient> patient = patientRepo.findPatientByCodePatient(assurancePatientRequestDto.getCodePatient());
        assuranceToSave.setPatient(patient.get());

        Optional<TypeAssurance> typeAssurance = typeAssuranceRepo.findTypeAssuranceByCodeAssuranceType(assurancePatientRequestDto.getCodeTypeAssurance());
        assuranceToSave.setTypeAssurance(typeAssurance.get());

        Optional<AssurancePatient> existingAssurance = assurancePatientRepository.findAssurancePatientByCodeAssurance(assuranceToSave.getCodeAssurance());
        if (existingAssurance.isPresent()) {
            throw new EntityAlreadyExisteException("Assurance de patient already exists with id: " + assurancePatientRequestDto.getCodeAssurance());
        }
        AssurancePatient savedAssurance = assurancePatientRepository.save(assuranceToSave);
        return assurancePatientMapper.modelToDto(savedAssurance);
    }

    @Override
    public AssurancePatientResponseDto updateAssurancePatient(AssurancePatientRequestDto assurancePatientRequestDto) {
        Optional<AssurancePatient> existingAssurance = assurancePatientRepository.findAssurancePatientByCodeAssurance(assurancePatientRequestDto.getCodeAssurance());
        if (existingAssurance.isEmpty()){
            throw new EntityNotFoundException("Assurance de patient non trouvé ");
        }
        AssurancePatient AssuranceToUpdate = assurancePatientMapper.dtoToModel(assurancePatientRequestDto);
        AssuranceToUpdate.setId(existingAssurance.get().getId());
        AssuranceToUpdate.setCodeAssurance(existingAssurance.get().getCodeAssurance());

        AssuranceToUpdate.setPatient(patientRepo.findPatientByCodePatient(assurancePatientRequestDto.getCodePatient()).get());
        AssuranceToUpdate.setTypeAssurance(typeAssuranceRepo.findTypeAssuranceByCodeAssuranceType(assurancePatientRequestDto.getCodeTypeAssurance()).get());

        AssurancePatient updatedAsssurance= assurancePatientRepository.save(AssuranceToUpdate);
        return assurancePatientMapper.modelToDto(updatedAsssurance);
    }

    @Override
    public void deleteAssurancePatient(String codeAssurancePatient) {
        Optional<AssurancePatient> assurancePatient = assurancePatientRepository.findAssurancePatientByCodeAssurance(codeAssurancePatient);
        if (assurancePatient.isEmpty()) {
            throw new EntityNotFoundException("Assurance de Patient non trouvé avec le code : " + codeAssurancePatient);
        }
        assurancePatientRepository.deleteByCodeAssurance(codeAssurancePatient);
    }



    @Override
    public Page<AssurancePatientResponseDto> findAssurancePatientByCriteria(AssurancePatientCriteria assurancePatientCriteria, int page, int size) {
        PageRequest pageable = PageRequest.of(page, size);
        Page<AssurancePatient> assurancePatientitauxPage = assurancePatientRepository.findAll((root, query, criteriaBuilder) -> {
            List<Predicate> predicateList = new ArrayList<>();
            if (assurancePatientCriteria.getId() != null){
                predicateList.add(criteriaBuilder.equal(root.get("id"),assurancePatientCriteria.getId()));
            }
            if (assurancePatientCriteria.getNumeroMatricule() != null){
                predicateList.add(criteriaBuilder.equal(root.get("numeroMatricule"),assurancePatientCriteria.getNumeroMatricule()));
            }
            if (assurancePatientCriteria.getDateDebutCouverture() != null){
                predicateList.add(criteriaBuilder.equal(root.get("dateDebutCouverture"),assurancePatientCriteria.getDateDebutCouverture()));
            }

            if (assurancePatientCriteria.getDateFinCouverture() != null){
                predicateList.add(criteriaBuilder.equal(root.get("dateFinCouverture"),assurancePatientCriteria.getDateFinCouverture()));
            }
            if (assurancePatientCriteria.getStatut() != null){
                predicateList.add(criteriaBuilder.equal(root.get("status"),assurancePatientCriteria.getStatut()));
            }

            if (assurancePatientCriteria.getCodeAssurance() != null){
                predicateList.add(criteriaBuilder.equal(root.get("codeAssurance"),assurancePatientCriteria.getCodeAssurance()));
            }

            return criteriaBuilder.and(predicateList.toArray(new Predicate[0]));

        } , pageable);
        return assurancePatientMapper.modelToDtos(assurancePatientitauxPage);
    }

    @Override
    public byte[] generateExcel() throws IOException {
        List<AssurancePatient> assurances = assurancePatientRepository.findAll();
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Assurances");
        Row headerRow = sheet.createRow(0);
        String[] headers = {
                "ID",
                "Code Assurance",
                "Numéro Matricule",
                "Date Début Couverture",
                "Date Fin Couverture",
                "Détails Couverture",
                "Statut",
                "Code Patient",
                "Code Type Assurance"
        };

        // Création de l'en-tête
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }

        int rowNum = 1;
        // Remplissage des données
        for (AssurancePatient assurance : assurances) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(assurance.getId());
            row.createCell(1).setCellValue(assurance.getCodeAssurance());
            row.createCell(2).setCellValue(assurance.getNumeroMatricule());
            row.createCell(3).setCellValue(assurance.getDateDebutCouverture().toString());
            row.createCell(4).setCellValue(assurance.getDateFinCouverture().toString());
            row.createCell(5).setCellValue(assurance.getDetailsCouverture());
            row.createCell(6).setCellValue(assurance.getStatut());
            row.createCell(7).setCellValue(assurance.getCodePatient());
            //row.createCell(8).setCellValue(assurance.getCodeTypeAssurance());
        }

        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            workbook.write(byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        }
    }
}
