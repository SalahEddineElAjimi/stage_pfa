package com.amasoft.amaclinic.Service.Impl;


import com.amasoft.amaclinic.Entity.Patient;
import com.amasoft.amaclinic.Service.PatientService;
import com.amasoft.amaclinic.criteria.PatientCriteria;
import com.amasoft.amaclinic.dto.request.PatientRequestDto;
import com.amasoft.amaclinic.dto.response.PatientResponseDto;
import com.amasoft.amaclinic.exception.EntityAlreadyExisteException;
import com.amasoft.amaclinic.exception.EntityNotFoundException;
import com.amasoft.amaclinic.mapper.PatientMapper;
import com.amasoft.amaclinic.repository.PatientRepository;
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
public class PatientServiceImpl implements PatientService {
    private PatientRepository patientRepository;
    private PatientMapper patientMapper;

    @Override
    public PatientResponseDto addPatient(PatientRequestDto patientRequestDto) {
        String generatedCodePatient = "PT" + UUID.randomUUID().toString().replace("-", "").substring(0, 8);
        Patient patientToSave = patientMapper.dtoToModel(patientRequestDto);
        patientToSave.setCodePatient(generatedCodePatient);

        Optional<Patient> existingpatient = patientRepository.findPatientByCodePatient(patientToSave.getCodePatient());
        if (existingpatient.isPresent()) {
            throw new EntityAlreadyExisteException("Patient existe déjà avec l'identifiant : " + patientRequestDto.getCodePatient());
        }
        Patient savedPatient = patientRepository.save(patientToSave);
        return patientMapper.modelToDto(savedPatient);
    }

    @Override
    public PatientResponseDto updatePatient(PatientRequestDto patientRequestDto) {
        Optional<Patient> existingPat = patientRepository.findPatientByCodePatient(patientRequestDto.getCodePatient());
        if (existingPat.isEmpty()){
            throw new EntityNotFoundException("Patient non trouvé ");
        }
        Patient patientToUpdate = patientMapper.dtoToModel(patientRequestDto);
        patientToUpdate.setId(existingPat.get().getId());
        patientToUpdate.setCodePatient(existingPat.get().getCodePatient());

        Patient updatedPatient= patientRepository.save(patientToUpdate);
        return patientMapper.modelToDto(updatedPatient);
    }

    @Override
    public void deletePatient(String codePatient) {
        Optional<Patient> patient = patientRepository.findPatientByCodePatient(codePatient);
        if (patient.isEmpty()){
            throw new EntityNotFoundException("Patient non trouvé avec le code :  "+codePatient);
        }

        patientRepository.deleteByCodePatient(codePatient);
    }


    @Override
    public Page<PatientResponseDto> findPatientByCriteria(PatientCriteria patientCriteria, int page, int size) {
        PageRequest pageable = PageRequest.of(page, size);
        Page<Patient> patientPage = patientRepository.findAll((root, query, criteriaBuilder) -> {
            List<Predicate> predicateList = new ArrayList<>();
            if (patientCriteria.getId() != null){
                predicateList.add(criteriaBuilder.equal(root.get("id"),patientCriteria.getId()));
            }
            if (patientCriteria.getPrenom() != null){
                predicateList.add(criteriaBuilder.equal(root.get("prenom"),patientCriteria.getPrenom()));
            }
            if (patientCriteria.getNom() != null){
                predicateList.add(criteriaBuilder.equal(root.get("nom"),patientCriteria.getNom()));
            }
            if (patientCriteria.getCin() != null){
                predicateList.add(criteriaBuilder.equal(root.get("cin"),patientCriteria.getCin()));
            }
            if (patientCriteria.getPatientCode() != null){
                predicateList.add(criteriaBuilder.equal(root.get("patientCode"),patientCriteria.getPatientCode()));
            }

            return criteriaBuilder.and(predicateList.toArray(new Predicate[0]));

        } , pageable);
        return patientMapper.modelToDtos(patientPage);
    }

    @Override
    public byte[] generatePatientExcel() throws IOException {
        List<Patient> patients = patientRepository.findAll();
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("patients");
        Row headerRow = sheet.createRow(0);
        String[] headers = {"Code Patient", "Prénom", "Nom", "CIN", "Sexe", "Téléphone", "Email", "Adresse", "Ville"};

        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }

        int rowNum = 1;
        for (Patient patient : patients) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(patient.getCodePatient());
            row.createCell(1).setCellValue(patient.getPrenom());
            row.createCell(2).setCellValue(patient.getNom());
            row.createCell(3).setCellValue(patient.getCin());
            row.createCell(4).setCellValue(patient.getSexe());
            row.createCell(5).setCellValue(patient.getTelephone());
            row.createCell(6).setCellValue(patient.getEmail());
            row.createCell(7).setCellValue(patient.getAddress());
            row.createCell(8).setCellValue(patient.getVille());
        }

        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            workbook.write(byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        }
    }
}
