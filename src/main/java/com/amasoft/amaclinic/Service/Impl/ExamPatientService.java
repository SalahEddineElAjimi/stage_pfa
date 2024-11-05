package com.amasoft.amaclinic.Service.Impl;

import com.amasoft.amaclinic.Entity.ExamPatient;
import com.amasoft.amaclinic.Entity.Patient;
import com.amasoft.amaclinic.criteria.ExamPatientCriteria;
import com.amasoft.amaclinic.dto.request.ExamPatientRequestDto;
import com.amasoft.amaclinic.dto.response.ExamPatientResponseDto;
import com.amasoft.amaclinic.exception.EntityAlreadyExisteException;
import com.amasoft.amaclinic.exception.EntityNotFoundException;
import com.amasoft.amaclinic.mapper.ExamPatientMapper;
import com.amasoft.amaclinic.repository.ExamPatientRepository;
import com.amasoft.amaclinic.repository.PatientRepository;
import jakarta.persistence.criteria.Join;
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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Service
@AllArgsConstructor
@Transactional
public class ExamPatientService implements com.amasoft.amaclinic.Service.ExamPatientService {
    private ExamPatientRepository examPatientRepository;
    private PatientRepository patientRepository ;
    private ExamPatientMapper examPatientMapper;

    @Override
    public ExamPatientResponseDto addExamPatient(ExamPatientRequestDto examPatientRequestDto) {
        String generatedCodeExam = "EXP" + UUID.randomUUID().toString().replace("-", "").substring(0, 8);
        ExamPatient examToSave = examPatientMapper.dtoToModel(examPatientRequestDto);
        examToSave.setCodeExamPatient(generatedCodeExam);
        examToSave.setDateExam(LocalDate.now());
        Optional<Patient> patient = patientRepository.findPatientByCodePatient(examPatientRequestDto.getCodePatient());
        if (patient.isEmpty()) {
            throw new EntityNotFoundException("Aucun patient trouvé avec le code : " + examPatientRequestDto.getCodePatient());
        }
        examToSave.setPatient(patient.get());
        Optional<ExamPatient> existingExam = examPatientRepository.findExamBycodeExamPatient(examToSave.getCodeExamPatient());
        if (existingExam.isPresent()) {
            throw new EntityAlreadyExisteException("exam already exists with id: " + examPatientRequestDto.getId());
        }
        ExamPatient savedExam = examPatientRepository.save(examToSave);
        return examPatientMapper.modelToDto(savedExam);
    }

    @Override
    public ExamPatientResponseDto updateExamPatient(ExamPatientRequestDto examPatientRequestDto) {

        Optional<ExamPatient> existingExamPatient = examPatientRepository.findExamBycodeExamPatient(examPatientRequestDto.getCodeExamPatient());

        if (existingExamPatient.isEmpty()) {
            throw new EntityNotFoundException("Exam not found with code: " + examPatientRequestDto.getCodeExamPatient());
        }

        ExamPatient examPatientToUpdate = examPatientMapper.dtoToModel(examPatientRequestDto);
        examPatientToUpdate.setCodeExamPatient(examPatientRequestDto.getCodeExamPatient());
        examPatientToUpdate.setId(existingExamPatient.get().getId());

        Optional<Patient> patient = patientRepository.findPatientByCodePatient(examPatientRequestDto.getCodePatient());
        if (patient.isEmpty()) {
            throw new EntityNotFoundException("Patient not found with code: " + examPatientRequestDto.getCodePatient());
        }
        examPatientToUpdate.setPatient(patient.get());

        ExamPatient updatedExamPatient = examPatientRepository.save(examPatientToUpdate);
        return examPatientMapper.modelToDto(updatedExamPatient);
    }

    @Override
    public void deleteExamPatient(String codeExamPatient) {
        Optional<ExamPatient> examPatient = examPatientRepository.findExamBycodeExamPatient(codeExamPatient);
        if (examPatient.isEmpty()){
            throw new EntityNotFoundException("Product Not Found Code :  "+codeExamPatient);
        }
        examPatientRepository.deleteBycodeExamPatient(codeExamPatient);
    }

    @Override
    public Page<ExamPatientResponseDto> findExamPatientByCriteria(ExamPatientCriteria examPatientCriteria, int page, int size) {
        PageRequest pageable = PageRequest.of(page, size);

        Page<ExamPatient> examPatientPage = examPatientRepository.findAll((root, query, criteriaBuilder) -> {
            List<Predicate> predicateList = new ArrayList<>();

            if (examPatientCriteria.getId() != null) {
                predicateList.add(criteriaBuilder.equal(root.get("id"), examPatientCriteria.getId()));
            }
            if (examPatientCriteria.getCodeExamPatient() != null) {
                predicateList.add(criteriaBuilder.equal(root.get("codeExam"), examPatientCriteria.getCodeExamPatient()));
            }
            if (examPatientCriteria.getDateExam() != null) {
                predicateList.add(criteriaBuilder.equal(root.get("dateExam"), examPatientCriteria.getDateExam()));
            }
            if (examPatientCriteria.getCodePatient() != null) {
                Join<ExamPatient, Patient> patientJoin = root.join("patient");
                predicateList.add(criteriaBuilder.equal(patientJoin.get("codePatient"), examPatientCriteria.getCodePatient()));
            }

            return criteriaBuilder.and(predicateList.toArray(new Predicate[0]));
        }, pageable);

        return examPatientMapper.modelToDtos(examPatientPage);
    }

    @Override
    public byte[] generatePatientExam() throws IOException {
        List<ExamPatient> exams = examPatientRepository.findAll();
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("ExamensPatients");
        Row headerRow = sheet.createRow(0);
        String[] headers = {"ID", "Code Examen", "Date Examen", "Détail", "Type", "Code Patient"};

        // Création de l'en-tête
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }

        int rowNum = 1;
        // Remplissage des données
        for (ExamPatient exam : exams) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(exam.getId());
            row.createCell(1).setCellValue(exam.getCodeExamPatient());
            row.createCell(2).setCellValue(exam.getDateExam().toString());
            row.createCell(3).setCellValue(exam.getDetail());
            row.createCell(4).setCellValue(exam.getType());
            row.createCell(5).setCellValue(exam.getPatient().getCodePatient());
        }

        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            workbook.write(byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        }
    }
}
