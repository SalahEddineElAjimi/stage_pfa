package com.amasoft.amaclinic.Service.Impl;

import com.amasoft.amaclinic.Entity.Consultation;
import com.amasoft.amaclinic.Entity.RendezVous;
import com.amasoft.amaclinic.Entity.Salle;
import com.amasoft.amaclinic.Entity.Paiment; // Assurez-vous d'importer l'entité Paiment
import com.amasoft.amaclinic.Service.ConsultationService;
import com.amasoft.amaclinic.criteria.ConsultationCriteria;
import com.amasoft.amaclinic.dto.request.ConsultationRequestDto;
import com.amasoft.amaclinic.dto.response.ConsultationResponseDTO;
import com.amasoft.amaclinic.exception.EntityAlreadyExisteException;
import com.amasoft.amaclinic.exception.EntityNotFoundException;
import com.amasoft.amaclinic.mapper.ConsultationMapper;
import com.amasoft.amaclinic.repository.ConsultationRepository;
import com.amasoft.amaclinic.repository.RendezVousRepository;
import com.amasoft.amaclinic.repository.SalleRepository;
import com.amasoft.amaclinic.repository.PaimentRepository; // Assurez-vous d'importer le repository Paiment
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
public class ConsultationServiceImpl implements ConsultationService {

    private final ConsultationRepository consultationRepository;
    private final RendezVousRepository rendezVousRepo;
    private final SalleRepository salleRepo;
    private final PaimentRepository paimentRepo; // Ajout du repository Paiment
    private final ConsultationMapper consultationMapper;

    @Override
    public ConsultationResponseDTO addConsultation(ConsultationRequestDto consultationRequestDto) {
        String generatedCodeConsultation = "CST" + UUID.randomUUID().toString().replace("-", "").substring(0, 8);
        Consultation consultationToSave = consultationMapper.dtoToModel(consultationRequestDto);
        consultationToSave.setCodeConsultation(generatedCodeConsultation);

        Optional<RendezVous> rendezVous = rendezVousRepo.findByCodeRendezVous(consultationRequestDto.getCodeRendezVous());
        consultationToSave.setRendezVous(rendezVous.get());

        Optional<Salle> salle = salleRepo.findByCodeSalle(consultationRequestDto.getCodeSalle());
        consultationToSave.setSalle(salle.get());



        Optional<Consultation> existingConsultation = consultationRepository.findConsultationByCodeConsultation(consultationToSave.getCodeConsultation());
        if (existingConsultation.isPresent()) {
            throw new EntityAlreadyExisteException("Consultation existe déjà avec l'identifiant : " + consultationRequestDto.getCodeConsultation());
        }

        Consultation savedConsultation = consultationRepository.save(consultationToSave);

        return consultationMapper.modelToDto(savedConsultation);
    }

    @Override
    public ConsultationResponseDTO UpdateConsultation(ConsultationRequestDto consultationRequestDto) {
        Optional<Consultation> existingCons = consultationRepository.findConsultationByCodeConsultation(consultationRequestDto.getCodeConsultation());
        if (existingCons.isEmpty()) {
            throw new EntityNotFoundException("Consultation non trouvée ");
        }

        Consultation consultationToUpdate = consultationMapper.dtoToModel(consultationRequestDto);

        consultationToUpdate.setId(existingCons.get().getId());
        consultationToUpdate.setCodeConsultation(existingCons.get().getCodeConsultation());
        consultationToUpdate.setRendezVous(rendezVousRepo.findByCodeRendezVous(consultationRequestDto.getCodeRendezVous()).get());
        consultationToUpdate.setSalle(salleRepo.findByCodeSalle(consultationRequestDto.getCodeSalle()).get());



        Consultation updatedConsultation = consultationRepository.save(consultationToUpdate);

        return consultationMapper.modelToDto(updatedConsultation);
    }

    @Override
    public void deleteConsultation(String codeConsultation) {
        Optional<Consultation> consultation = consultationRepository.findConsultationByCodeConsultation(codeConsultation);
        if (consultation.isEmpty()) {
            throw new EntityNotFoundException("Consultation non trouvée avec le code :  " + codeConsultation);
        }

        consultationRepository.deleteConsultationByCodeConsultation(codeConsultation);
    }

    @Override
    public Page<ConsultationResponseDTO> findConsultationByCriteria(ConsultationCriteria consultationCriteria, int page, int size) {
        PageRequest pageable = PageRequest.of(page, size);
        Page<Consultation> consultationPage = consultationRepository.findAll((root, query, criteriaBuilder) -> {
            List<Predicate> predicateList = new ArrayList<>();
            if (consultationCriteria.getId() != null) {
                predicateList.add(criteriaBuilder.equal(root.get("id"), consultationCriteria.getId()));
            }
            if (consultationCriteria.getDate() != null) {
                predicateList.add(criteriaBuilder.equal(root.get("date"), consultationCriteria.getDate()));
            }

            if (consultationCriteria.getCodeConsultation() != null) {
                predicateList.add(criteriaBuilder.equal(root.get("codeConsultation"), consultationCriteria.getCodeConsultation()));
            }

            return criteriaBuilder.and(predicateList.toArray(new Predicate[0]));
        }, pageable);

        return consultationMapper.modelToDtos(consultationPage);
    }

    @Override
    public byte[] generateConsultation() throws IOException {
        List<Consultation> consultations = consultationRepository.findAll();
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Consultations");
        Row headerRow = sheet.createRow(0);
        String[] headers = {"ID Consultation", "Code Consultation", "Date", "Code RendezVous", "Code Salle", "Paiement"};

        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }

        int rowNum = 1;
        for (Consultation consultation : consultations) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(consultation.getId());
            row.createCell(1).setCellValue(consultation.getCodeConsultation());
            row.createCell(2).setCellValue(consultation.getDate().toString());
            row.createCell(3).setCellValue(consultation.getRendezVous().getCodeRendezVous());
            row.createCell(4).setCellValue(consultation.getSalle().getCodeSalle());

            // Gestion du champ paiement (si non null)
            if (consultation.getPaiment() != null) {
                row.createCell(5).setCellValue(consultation.getPaiment().getCodePaiment());
            } else {
                row.createCell(5).setCellValue("Aucun paiement");
            }
        }

        // Écriture du fichier Excel en mémoire et retour en tableau d'octets
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            workbook.write(byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        }
    }
}
