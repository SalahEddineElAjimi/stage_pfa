package com.amasoft.amaclinic.Service.Impl;

import com.amasoft.amaclinic.Entity.*;
import com.amasoft.amaclinic.Service.RendezVousService;
import com.amasoft.amaclinic.criteria.RendezVousCriteria;
import com.amasoft.amaclinic.dto.request.RendezVousRequestDTO;
import com.amasoft.amaclinic.dto.response.RendezVousResponseDTO;
import com.amasoft.amaclinic.exception.EntityAlreadyExisteException;
import com.amasoft.amaclinic.exception.EntityNotFoundException;
import com.amasoft.amaclinic.mapper.RendezVousMapper;
import com.amasoft.amaclinic.repository.*;
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
public class RendezVousServiceImpl implements RendezVousService {
    private RendezVousRepository rendezVousRepository;
    private PatientRepository patientRepo;
    private MedecinRepository medecinRepo;
    private RendezVousMapper rendezVousMapper;
    @Override
    public RendezVousResponseDTO addRendezVous(RendezVousRequestDTO rendezVousRequestDto) {
        if (rendezVousRequestDto.getDateDebutRDV().isAfter(rendezVousRequestDto.getDateFinRDV())){
            throw new IllegalArgumentException("La date de début du rendez-vous doit être inférieure à la date de fin.");
        }

        List<RendezVous> overlappingRDVs = rendezVousRepository.findOverlappingRendezVous(
                rendezVousRequestDto.getCodeMedecin(),
                rendezVousRequestDto.getDateDebutRDV(),
                rendezVousRequestDto.getDateFinRDV()
        );

        if (!overlappingRDVs.isEmpty()) {
            throw new EntityNotFoundException("Le médecin n'est pas disponible dans l'intervalle de temps sélectionné.");
        }


        String generatedCodeRendezVous = "RDV" + UUID.randomUUID().toString().replace("-", "").substring(0, 7);
        RendezVous rendezVousTosave = rendezVousMapper.dtoToModel(rendezVousRequestDto);
        rendezVousTosave.setCodeRendezVous(generatedCodeRendezVous);

        Optional<Patient> patient = patientRepo.findPatientByCodePatient(rendezVousRequestDto.getCodePatient());
        rendezVousTosave.setPatient(patient.get());

        Optional<Medecin> medecin = medecinRepo.findMedecinByCodeMedecin(rendezVousRequestDto.getCodeMedecin());
        rendezVousTosave.setMedecin(medecin.get());



        Optional<RendezVous> existingRendezVous = rendezVousRepository.findByCodeRendezVous(rendezVousTosave.getCodeRendezVous());
        if (existingRendezVous.isPresent()) {
            throw new EntityAlreadyExisteException("RendezVous existe déjà avec l'identifiant: " + rendezVousRequestDto.getId());
        }

        RendezVous savedRDV = rendezVousRepository.save(rendezVousTosave);
        return rendezVousMapper.modelToDto(savedRDV);
    }

    @Override
    public RendezVousResponseDTO updateRendezVous(RendezVousRequestDTO rendezVousRequestDto) {
        if (rendezVousRequestDto.getDateDebutRDV().isAfter(rendezVousRequestDto.getDateFinRDV())) {
            throw new IllegalArgumentException("La date de début du rendez-vous doit être antérieure à la date de fin.");
        }

        Optional<RendezVous> existingRDV = rendezVousRepository.findByCodeRendezVous(rendezVousRequestDto.getCodeRendezVous());
        if (existingRDV.isEmpty()){
            throw new EntityNotFoundException("RendezVous  non trouvé ");
        }

        List<RendezVous> overlappingRDVs = rendezVousRepository.findOverlappingRendezVousExcludingCurrent(
                rendezVousRequestDto.getCodeMedecin(),
                rendezVousRequestDto.getDateDebutRDV(),
                rendezVousRequestDto.getDateFinRDV(),
                rendezVousRequestDto.getCodeRendezVous()
        );

        if (!overlappingRDVs.isEmpty()) {
            throw new EntityNotFoundException("Le médecin n'est pas disponible dans l'intervalle de temps sélectionné.");
        }

        RendezVous RDVToUpdate = rendezVousMapper.dtoToModel(rendezVousRequestDto);
        RDVToUpdate.setId(existingRDV.get().getId());
        RDVToUpdate.setCodeRendezVous(existingRDV.get().getCodeRendezVous());

        RDVToUpdate.setPatient(patientRepo.findPatientByCodePatient(rendezVousRequestDto.getCodePatient()).get());
        RDVToUpdate.setMedecin(medecinRepo.findMedecinByCodeMedecin(rendezVousRequestDto.getCodeMedecin()).get());

        RendezVous updatedRDV= rendezVousRepository.save(RDVToUpdate);
        return rendezVousMapper.modelToDto(updatedRDV);    }

    @Override
    public void deleteRendezVous(String codeRDV) {
        Optional<RendezVous> rendezVous = rendezVousRepository.findByCodeRendezVous(codeRDV);
        if (rendezVous.isEmpty()) {
            throw new EntityNotFoundException("RendezVous  non trouvé avec le code : " + codeRDV);
        }
        rendezVousRepository.deleteByCodeRendezVous(codeRDV);

    }

    @Override
    public Page<RendezVousResponseDTO> findRendezVousByCriteria(RendezVousCriteria rendezVousCriteria, int page, int size) {
        PageRequest pageable = PageRequest.of(page, size);
        Page<RendezVous> rendezVousPage = rendezVousRepository.findAll((root, query, criteriaBuilder) -> {
            List<Predicate> predicateList = new ArrayList<>();
            if (rendezVousCriteria.getId() != null){
                predicateList.add(criteriaBuilder.equal(root.get("id"),rendezVousCriteria.getId()));
            }

            if (rendezVousCriteria.getStatus() != null){
                predicateList.add(criteriaBuilder.equal(root.get("status"),rendezVousCriteria.getStatus()));
            }

            if (rendezVousCriteria.getTypeRDV() != null){
                predicateList.add(criteriaBuilder.equal(root.get("typeRDV"),rendezVousCriteria.getTypeRDV()));
            }
            if (rendezVousCriteria.getDateDebutRDV()!=null){
                predicateList.add(criteriaBuilder.equal(root.get("dateDebutRDV"),rendezVousCriteria.getDateDebutRDV()));
            }
            if (rendezVousCriteria.getDateFinRDV()!=null){
                predicateList.add(criteriaBuilder.equal(root.get("dateFinRDV"),rendezVousCriteria.getDateFinRDV()));
            }


            if (rendezVousCriteria.getCodeRendezVous() != null){
                predicateList.add(criteriaBuilder.equal(root.get("codeRendezVous"),rendezVousCriteria.getCodeRendezVous()));
            }

            return criteriaBuilder.and(predicateList.toArray(new Predicate[0]));

        } , pageable);
        return rendezVousMapper.modelToDtos(rendezVousPage);
    }

    @Override
    public byte[] generateRdvExcel() throws IOException {
        List<RendezVous> rendezVousList = rendezVousRepository.findAll();
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("RendezVous");
        Row headerRow = sheet.createRow(0);
        String[] headers = {
                "Code RendezVous", "Status", "Type RDV", "Date Début RDV", "Date Fin RDV",
                "Code Patient", "Code Medecin"
        };

        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }

        int rowNum = 1;
        for (RendezVous rendezVous : rendezVousList) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(rendezVous.getCodeRendezVous());
            row.createCell(1).setCellValue(rendezVous.getStatus());
            row.createCell(2).setCellValue(rendezVous.getTypeRDV());
            row.createCell(3).setCellValue(rendezVous.getDateDebutRDV().toString());
            row.createCell(4).setCellValue(rendezVous.getDateFinRDV().toString());
            row.createCell(5).setCellValue(rendezVous.getPatient().getCodePatient());
            row.createCell(6).setCellValue(rendezVous.getMedecin().getCodeMedecin());
        }

        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            workbook.write(byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        }
    }
}

