package com.amasoft.amaclinic.Service.Impl;

import com.amasoft.amaclinic.Entity.*;
import com.amasoft.amaclinic.Service.OrdonnanceService;
import com.amasoft.amaclinic.criteria.OrdonnanceCriteria;
import com.amasoft.amaclinic.dto.MedicamentDetailsDTO;
import com.amasoft.amaclinic.dto.request.OrdonnanceRequestDTO;
import com.amasoft.amaclinic.dto.response.OrdonnanceResponseDTO;
import com.amasoft.amaclinic.exception.EntityNotFoundException;
import com.amasoft.amaclinic.mapper.OrdonnanceMapper;
import com.amasoft.amaclinic.repository.ConsultationRepository;
import com.amasoft.amaclinic.repository.OrdonnanceRepository;
import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.http.HttpHeaders;
import net.sf.jasperreports.engine.JRException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Service
@AllArgsConstructor
@Transactional
public class OrdonnanceServiceImpl implements OrdonnanceService {
    private OrdonnanceRepository ordonnanceRepository;
    private ConsultationRepository consultationRepo;

    private OrdonnanceMapper ordonnanceMapper;



    @Override
    public ResponseEntity<byte[]> generateOrdonnanceReport(String code) throws JRException, IOException {
        Optional<Ordonnance> ordonnance = ordonnanceRepository.findByCodeOrdonnance(code);
        if (ordonnance.isEmpty()) {
            throw new RuntimeException("Ordonnance avec le code '" + code + "' non trouvée.");
        }

        Ordonnance ord = ordonnance.get();
        Medecin medecin = ord.getConsultation().getRendezVous().getMedecin();
        if (medecin == null || medecin.getNom() == null || medecin.getPrenom() == null || medecin.getTelephone() == null) {
            throw new RuntimeException("\"Données incomplètes pour le rapport : Les informations du médecin sont manquantes.");
        }

        Cabinet cabinet = ord.getConsultation().getSalle().getCabinet();
        if (cabinet == null || cabinet.getNomCabinet() == null) {
            throw new RuntimeException("Données incomplètes pour le rapport : Les informations sur le cabinet sont manquantes.");
        }

        Patient patient = ord.getConsultation().getRendezVous().getPatient();
        if (patient == null || patient.getNom() == null || patient.getPrenom() == null || patient.getAge() == null) {
            throw new RuntimeException("Données incomplètes pour le rapport :  Les informations sur le patient sont manquantes.");
        }
        InputStream logoStream = getClass().getResourceAsStream("/images/logo.png");
        if (logoStream == null) {
            throw new RuntimeException("Image du logo non trouvée à l'emplacement spécifié.");
        }

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("cabinetName", cabinet.getNomCabinet());
        parameters.put("Nom_Prenom_med", medecin.getNom() + " " + medecin.getPrenom());
        parameters.put("Tele", medecin.getTelephone());
        parameters.put("CabinetAddress", cabinet.getAdresse());
        parameters.put("PatientName", patient.getNom() + " " + patient.getPrenom());
        parameters.put("Age", patient.getAge());
        parameters.put("Date", ord.getDateOrdonnance());
        parameters.put("signature",medecin.getNom() + " " + medecin.getPrenom());
        parameters.put("email_cab",cabinet.getEmail());
        parameters.put("logo", logoStream);

        List<MedicamentDetailsDTO> medicamentDetailsList = ordonnanceRepository.findMedicamentDetailsByCode(code);
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(medicamentDetailsList);

        InputStream reportStream = getClass().getResourceAsStream("/pdf/OrdonnanceReport.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters,  dataSource);

        byte[] pdfData;
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            JasperExportManager.exportReportToPdfStream(jasperPrint, byteArrayOutputStream);
            pdfData = byteArrayOutputStream.toByteArray();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "ordonnance_report.pdf");

        return new ResponseEntity<>(pdfData, headers, HttpStatus.OK);
    }



    @Override
    public OrdonnanceResponseDTO addOrdonnance(OrdonnanceRequestDTO ordonnanceRequestDto) {
        String generatedCodeOrdonnance = "ORD" + UUID.randomUUID().toString().replace("-", "").substring(0, 8);
        Ordonnance ordonnanceToSave = ordonnanceMapper.dtoToModel(ordonnanceRequestDto);
        ordonnanceToSave.setCodeOrdonnance(generatedCodeOrdonnance);

        Consultation consultation = consultationRepo.findConsultationByCodeConsultation(ordonnanceRequestDto.getCodeConsultation())
                .orElseThrow(() -> new EntityNotFoundException("Consultation not found with code: " + ordonnanceRequestDto.getCodeConsultation()));
        ordonnanceToSave.setConsultation(consultation);

        Ordonnance savedOrdonnance = ordonnanceRepository.save(ordonnanceToSave);
        return ordonnanceMapper.modelToDto(savedOrdonnance);
    }


    @Override
    public OrdonnanceResponseDTO updateOrdonnance(OrdonnanceRequestDTO ordonnanceRequestDto) {
        Optional<Ordonnance> existingord = ordonnanceRepository.findByCodeOrdonnance(ordonnanceRequestDto.getCodeOrdonnance());
        if (existingord.isEmpty()) {
            throw new EntityNotFoundException("Ordonnance non trouvé ");
        }
        Ordonnance ordonnanceToUpdate = ordonnanceMapper.dtoToModel(ordonnanceRequestDto);
        ordonnanceToUpdate.setId(existingord.get().getId());
        ordonnanceToUpdate.setCodeOrdonnance(existingord.get().getCodeOrdonnance());

        Consultation consultation = consultationRepo.findConsultationByCodeConsultation(ordonnanceRequestDto.getCodeConsultation())
                .orElseThrow(() -> new EntityNotFoundException("Consultation not found with code: " + ordonnanceRequestDto.getCodeConsultation()));
        ordonnanceToUpdate.setConsultation(consultation);


        Ordonnance updatedOrdonnance = ordonnanceRepository.save(ordonnanceToUpdate);
        return ordonnanceMapper.modelToDto(updatedOrdonnance);

    }


    @Override
    public void deleteOrdonnance(String codeOrdonnance) {
        Optional<Ordonnance> ordonnance = ordonnanceRepository.findByCodeOrdonnance(codeOrdonnance);
        if (ordonnance.isEmpty()) {
            throw new EntityNotFoundException("Ordonnance non trouvé avec le code :  " + codeOrdonnance);
        }

        ordonnanceRepository.deleteByCodeOrdonnance(codeOrdonnance);
    }


    @Override
    public Page<OrdonnanceResponseDTO> findOrdonnanceByCriteria(OrdonnanceCriteria ordonnanceCriteria, int page, int size) {
        PageRequest pageable = PageRequest.of(page, size);
        Page<Ordonnance> ordonnancePage = ordonnanceRepository.findAll((root, query, criteriaBuilder) -> {
            List<Predicate> predicateList = new ArrayList<>();
            if (ordonnanceCriteria.getId() != null) {
                predicateList.add(criteriaBuilder.equal(root.get("id"), ordonnanceCriteria.getId()));
            }
            if (ordonnanceCriteria.getDateOrdonnance() != null) {
                predicateList.add(criteriaBuilder.equal(root.get("dateOrdonnance"), ordonnanceCriteria.getDateOrdonnance()));
            }
            if (ordonnanceCriteria.getDetailsOrdonnance() != null) {
                predicateList.add(criteriaBuilder.equal(root.get("detailsOrdonnance"), ordonnanceCriteria.getDetailsOrdonnance()));
            }

            if (ordonnanceCriteria.getCodeOrdonnance() != null) {
                predicateList.add(criteriaBuilder.equal(root.get("ordonnanceCode"), ordonnanceCriteria.getCodeOrdonnance()));
            }

            return criteriaBuilder.and(predicateList.toArray(new Predicate[0]));

        }, pageable);
        return ordonnanceMapper.modelToDtos(ordonnancePage);
    }


    @Override
    public byte[] generateOrdonnanceEXCEL() throws IOException {
        List<Ordonnance> ordonnances = ordonnanceRepository.findAll();
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Ordonnances");
        Row headerRow = sheet.createRow(0);
        String[] headers = {"ID", "Code Ordonnance", "Détails", "Date Ordonnance", "Code Consultation", "Codes Médicaments"};

        // Création de l'en-tête
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }

        int rowNum = 1;
        // Remplissage des données
        for (Ordonnance ordonnance : ordonnances) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(ordonnance.getId());
            row.createCell(1).setCellValue(ordonnance.getCodeOrdonnance());
            row.createCell(2).setCellValue(ordonnance.getDetailsOrdonnance());
            row.createCell(3).setCellValue(ordonnance.getDateOrdonnance().toString());
            row.createCell(4).setCellValue(ordonnance.getConsultation().getCodeConsultation());

            /* Gestion des codes médicaments sous forme de chaîne séparée par des virgules
            String medicamentCodes = String.join(", ", ordonnance.getMedicamentsCodes());
            row.createCell(5).setCellValue(medicamentCodes);*/
        }

        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            workbook.write(byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        }
    }
}

