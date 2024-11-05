package com.amasoft.amaclinic.Service.Impl;

import com.amasoft.amaclinic.Entity.*;
import com.amasoft.amaclinic.criteria.PaimentCriteria;
import com.amasoft.amaclinic.dto.request.PaimentRequestDto;
import com.amasoft.amaclinic.dto.response.PaimentResponseDto;

import com.amasoft.amaclinic.exception.EntityAlreadyExisteException;
import com.amasoft.amaclinic.exception.EntityNotFoundException;
import com.amasoft.amaclinic.mapper.PaimentMapper;
import com.amasoft.amaclinic.repository.MedecinRepository;
import com.amasoft.amaclinic.repository.PaimentRepository;
import com.amasoft.amaclinic.repository.ConsultationRepository;
import com.amasoft.amaclinic.Service.PaimentService;
import com.amasoft.amaclinic.repository.PatientRepository;
import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

import net.sf.jasperreports.engine.*;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
@Transactional
public class PaimentServiceImpl implements PaimentService {

    private final PaimentRepository paimentRepository;
    private final ConsultationRepository consultationRepository;
    private final PaimentMapper paimentMapper;


    private final PatientRepository patientRepository; // Assurez-vous que ce repository existe
    private final MedecinRepository medecinRepository; // Assurez-vous que ce repository existe




    @Override
    public ResponseEntity<byte[]> generatePaymentReceiptReport(String code) throws JRException, IOException {
        Optional<Paiment> paiementOpt = paimentRepository.findByCodePaiment(code);
        if (paiementOpt.isEmpty()) {
            throw new RuntimeException("Paiement non trouvé");
        }

        Paiment paiement = paiementOpt.get();
        Consultation consultation = paiement.getConsultation();
        if (consultation == null || consultation.getRendezVous() == null ||
                consultation.getRendezVous().getMedecin() == null ||
                consultation.getRendezVous().getMedecin().getNom() == null ||
                consultation.getRendezVous().getMedecin().getPrenom() == null) {
            throw new RuntimeException("Données incomplètes pour le rapport");
        }

        Patient patient = consultation.getRendezVous().getPatient();
        Medecin medecin = consultation.getRendezVous().getMedecin();

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("nomPatient", patient.getNom());
        parameters.put("prenomPatient", patient.getPrenom());
        parameters.put("nomMedecin", medecin.getNom());
        parameters.put("prenomMedecin", medecin.getPrenom());
        parameters.put("dateConsultation", consultation.getDate());
        parameters.put("montant", paiement.getMontant());

        try {

            InputStream reportStream = this.getClass().getResourceAsStream("/pdf/RecuPaiment.jrxml");
            if (reportStream == null) {
                throw new RuntimeException("Fichier de rapport non trouvé dans le classpath : RecuPaiment.jrxml");
            }

            JasperReport jasperReport;
            try {
                jasperReport = JasperCompileManager.compileReport(reportStream);
            } catch (JRException e) {
                throw new RuntimeException("Erreur lors de la compilation du rapport : " + e.getMessage(), e);
            }

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, new JREmptyDataSource());

            byte[] pdfData;
            try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
                JasperExportManager.exportReportToPdfStream(jasperPrint, byteArrayOutputStream);
                pdfData = byteArrayOutputStream.toByteArray();
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", "recu_paiement.pdf");

            return new ResponseEntity<>(pdfData, headers, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la génération du rapport de paiement", e);
        }
    }

    @Override
    public PaimentResponseDto creerPaiment(PaimentRequestDto paimentRequestDto) {
        String codePaimentGenere = "PAY" + UUID.randomUUID().toString().replace("-", "").substring(0, 8);
        Paiment paimentASauvegarder = paimentMapper.toEntity(paimentRequestDto);
        paimentASauvegarder.setCodePaiment(codePaimentGenere);

        Optional<Consultation> consultation = consultationRepository.findConsultationByCodeConsultation(paimentRequestDto.getCodeConsultation());
        if (consultation.isEmpty()) {
            throw new EntityNotFoundException("Consultation non trouvée avec le code : " + paimentRequestDto.getCodeConsultation());
        }
        paimentASauvegarder.setConsultation(consultation.get());

        Optional<Paiment> paimentExistant = paimentRepository.findByCodePaiment(paimentASauvegarder.getCodePaiment());
        if (paimentExistant.isPresent()) {
            throw new EntityAlreadyExisteException("Paiement existe déjà avec l'identifiant : " + paimentASauvegarder.getCodePaiment());
        }

        Paiment paimentSauvegarde = paimentRepository.save(paimentASauvegarder);
        return paimentMapper.toResponseDto(paimentSauvegarde);
    }

    @Override
    public PaimentResponseDto mettreAJourPaiment(String codePaiment, PaimentRequestDto paimentRequestDto) {
        Paiment paimentAmettreAJour = paimentRepository.findByCodePaiment(codePaiment)
                .orElseThrow(() -> new EntityNotFoundException("Paiement non trouvé avec le code : " + codePaiment));

        // Mise à jour des attributs
        paimentAmettreAJour.setCodePaiment(paimentRequestDto.getCodePaiment());
        paimentAmettreAJour.setDatePaiment(paimentRequestDto.getDatePaiment());
        paimentAmettreAJour.setMontant(paimentRequestDto.getMontant());
        paimentAmettreAJour.setTypePaiment(paimentRequestDto.getTypePaiment());

        // Mise à jour de la consultation
        if (paimentRequestDto.getCodeConsultation() != null) {
            Optional<Consultation> consultation = consultationRepository.findConsultationByCodeConsultation(paimentRequestDto.getCodeConsultation());
            if (consultation.isEmpty()) {
                throw new EntityNotFoundException("Consultation non trouvée avec le code : " + paimentRequestDto.getCodeConsultation());
            }
            paimentAmettreAJour.setConsultation(consultation.get());
        }

        Paiment paimentMisAJour = paimentRepository.save(paimentAmettreAJour);
        return paimentMapper.toResponseDto(paimentMisAJour);
    }

    @Override
    public void supprimerPaimentParCode(String codePaiment) {
        if (!paimentRepository.existsByCodePaiment(codePaiment)) {
            throw new EntityNotFoundException("Paiement non trouvé avec le code : " + codePaiment);
        }
        paimentRepository.deleteByCodePaiment(codePaiment);
    }

    @Override
    public Optional<PaimentResponseDto> obtenirPaimentParCode(String codePaiment) {
        return paimentRepository.findByCodePaiment(codePaiment)
                .map(paimentMapper::toResponseDto);
    }

    @Override
    public Page<PaimentResponseDto> trouverPaimentParCritères(PaimentCriteria paimentCriteria, int page, int size) {
        PageRequest pageable = PageRequest.of(page, size);
        Page<Paiment> paimentPage = paimentRepository.findAll((root, query, criteriaBuilder) -> {
            List<Predicate> predicateList = new ArrayList<>();

            // Filtrage par code de paiement
            if (paimentCriteria.getCodePaiment() != null) {
                predicateList.add(criteriaBuilder.equal(root.get("codePaiment"), paimentCriteria.getCodePaiment()));
            }

            // Filtrage par date de paiement (après une certaine date)
            if (paimentCriteria.getDatePaimentFrom() != null) {
                predicateList.add(criteriaBuilder.greaterThanOrEqualTo(root.get("date"), paimentCriteria.getDatePaimentFrom()));
            }

            // Filtrage par date de paiement (avant une certaine date)
            if (paimentCriteria.getDatePaimentTo() != null) {
                predicateList.add(criteriaBuilder.lessThanOrEqualTo(root.get("date"), paimentCriteria.getDatePaimentTo()));
            }

            // Filtrage par montant minimum
            if (paimentCriteria.getMontantMin() != null) {
                predicateList.add(criteriaBuilder.greaterThanOrEqualTo(root.get("montant"), paimentCriteria.getMontantMin()));
            }

            // Filtrage par montant maximum
            if (paimentCriteria.getMontantMax() != null) {
                predicateList.add(criteriaBuilder.lessThanOrEqualTo(root.get("montant"), paimentCriteria.getMontantMax()));
            }

            // Filtrage par type de paiement
            if (paimentCriteria.getTypePaiment() != null) {
                predicateList.add(criteriaBuilder.equal(root.get("typePaiment"), paimentCriteria.getTypePaiment()));
            }

            // Filtrage par code de consultation associé
            if (paimentCriteria.getCodeConsultation() != null) {
                predicateList.add(criteriaBuilder.equal(root.get("consultation.codeConsultation"), paimentCriteria.getCodeConsultation()));
            }

            // Retourner la combinaison de tous les prédicats
            return criteriaBuilder.and(predicateList.toArray(new Predicate[0]));
        }, pageable);

        return paimentMapper.toResponseDtoPage(paimentPage);
    }

    @Override
    public byte[] generatePaiment() throws IOException {
        List<Paiment> paiements = paimentRepository.findAll();
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Paiements");
        Row headerRow = sheet.createRow(0);
        String[] headers = {"Code Paiement", "Date Paiement", "Montant", "Type Paiement", "Code Consultation"};

        // Création de l'en-tête
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }

        int rowNum = 1;
        // Remplissage des données
        for (Paiment paiement : paiements) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(paiement.getCodePaiment());
            row.createCell(1).setCellValue(paiement.getDatePaiment().toString()); // Convertir LocalDateTime en String
            row.createCell(2).setCellValue(paiement.getMontant());
            row.createCell(3).setCellValue(paiement.getTypePaiment());
            row.createCell(4).setCellValue(paiement.getConsultation().getCodeConsultation());
        }

        // Écriture du fichier Excel en mémoire et retour en tableau d'octets
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            workbook.write(byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        }
    }
}
