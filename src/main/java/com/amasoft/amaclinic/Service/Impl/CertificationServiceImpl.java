package com.amasoft.amaclinic.Service.Impl;

import com.amasoft.amaclinic.Entity.Certification;
import com.amasoft.amaclinic.Entity.Consultation;
import com.amasoft.amaclinic.criteria.CertificationCriteria;
import com.amasoft.amaclinic.dto.request.CertificationRequestDto;
import com.amasoft.amaclinic.dto.response.CertificationResponseDTO;
import com.amasoft.amaclinic.exception.EntityAlreadyExisteException;
import com.amasoft.amaclinic.exception.EntityNotFoundException;
import com.amasoft.amaclinic.mapper.CertificationMapper;
import com.amasoft.amaclinic.repository.CertificationRepository;
import com.amasoft.amaclinic.repository.ConsultationRepository;
import com.amasoft.amaclinic.Service.CertificationService;
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
public class CertificationServiceImpl implements CertificationService {

    private final CertificationRepository certificationRepository;
    private final ConsultationRepository consultationRepository;
    private final CertificationMapper certificationMapper;

    @Override
    public CertificationRequestDto creerCertification(CertificationRequestDto certificationRequestDto) {
        String codeCertificationGenere = "CERT" + UUID.randomUUID().toString().replace("-", "").substring(0, 8);
        Certification certificationASauvegarder = certificationMapper.dtoToModel(certificationRequestDto);
        certificationASauvegarder.setCode(codeCertificationGenere);

        Optional<Consultation> consultation = consultationRepository.findConsultationByCodeConsultation(certificationRequestDto.getConsultationCode());
        if (consultation.isEmpty()) {
            throw new EntityNotFoundException("Consultation non trouvée avec le code : " + certificationRequestDto.getConsultationCode());
        }
        certificationASauvegarder.setConsultation(consultation.get());

        Optional<Certification> certificationExistant = certificationRepository.findByCode(certificationASauvegarder.getCode());
        if (certificationExistant.isPresent()) {
            throw new EntityAlreadyExisteException("Certification existe déjà avec le code : " + certificationASauvegarder.getCode());
        }

        Certification certificationSauvegarde = certificationRepository.save(certificationASauvegarder);
        return certificationMapper.modelToDto(certificationSauvegarde);
    }

    @Override
    public CertificationRequestDto mettreAJourCertification(String codeCertification, CertificationRequestDto certificationRequestDto) {
        Certification certificationAMettreAJour = certificationRepository.findByCode(codeCertification)
                .orElseThrow(() -> new EntityNotFoundException("Certification non trouvée avec le code : " + codeCertification));

        // Mise à jour des attributs
        certificationAMettreAJour.setType(certificationRequestDto.getType());
        certificationAMettreAJour.setDateDebut(certificationRequestDto.getDateDebut());
        certificationAMettreAJour.setDateFin(certificationRequestDto.getDateFin());

        // Mise à jour de la consultation
        if (certificationRequestDto.getConsultationCode() != null) {
            Optional<Consultation> consultation = consultationRepository.findConsultationByCodeConsultation(certificationRequestDto.getConsultationCode());
            if (consultation.isEmpty()) {
                throw new EntityNotFoundException("Consultation non trouvée avec le code : " + certificationRequestDto.getConsultationCode());
            }
            certificationAMettreAJour.setConsultation(consultation.get());
        }

        Certification certificationMisAJour = certificationRepository.save(certificationAMettreAJour);
        return certificationMapper.modelToDto(certificationMisAJour);
    }

    @Override
    public void supprimerCertificationParCode(String codeCertification) {
        if (!certificationRepository.existsByCode(codeCertification)) {
            throw new EntityNotFoundException("Certification non trouvée avec le code : " + codeCertification);
        }
        certificationRepository.deleteByCode(codeCertification);
    }

    @Override
    public Optional<CertificationRequestDto> obtenirCertificationParCode(String codeCertification) {
        return certificationRepository.findByCode(codeCertification)
                .map(certificationMapper::modelToDto);
    }

    @Override
    public Page<CertificationResponseDTO> trouverCertificationParCritères(CertificationCriteria certificationCriteria, int page, int size) {
        PageRequest pageable = PageRequest.of(page, size);
        Page<Certification> certificationPage = certificationRepository.findAll((root, query, criteriaBuilder) -> {
            List<Predicate> predicateList = new ArrayList<>();

            // Filtrage par code de certification
            if (certificationCriteria.getCode() != null) {
                predicateList.add(criteriaBuilder.equal(root.get("code"), certificationCriteria.getCode()));
            }

            // Filtrage par type de certification
            if (certificationCriteria.getType() != null) {
                predicateList.add(criteriaBuilder.equal(root.get("type"), certificationCriteria.getType()));
            }

            // Filtrage par code de consultation associé
            if (certificationCriteria.getConsultationCode() != null) {
                predicateList.add(criteriaBuilder.equal(root.get("consultation.codeConsultation"), certificationCriteria.getConsultationCode()));
            }

            // Filtrage par date de début
            if (certificationCriteria.getDateDebut() != null) {
                predicateList.add(criteriaBuilder.greaterThanOrEqualTo(root.get("dateDebut"), certificationCriteria.getDateDebut()));
            }

            // Filtrage par date de fin
            if (certificationCriteria.getDateFin() != null) {
                predicateList.add(criteriaBuilder.lessThanOrEqualTo(root.get("dateFin"), certificationCriteria.getDateFin()));
            }

            // Retourner la combinaison de tous les prédicats
            return criteriaBuilder.and(predicateList.toArray(new Predicate[0]));
        }, pageable);

        return certificationMapper.toResponseDtoPage(certificationPage);
    }

    @Override
    public byte[] generateCertificationExcel() throws IOException {
        List<Certification> certifications = certificationRepository.findAll();

        // Création d'un nouveau workbook et d'une feuille
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Certifications");

        // Création de la ligne d'en-tête
        Row headerRow = sheet.createRow(0);
        String[] headers = {
                "Code",
                "Type",
                "Code Consultation",
                "Date Début",
                "Date Fin"
        };

        // Remplissage de la ligne d'en-tête
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }

        int rowNum = 1;
        // Remplissage des données
        for (Certification certification : certifications) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(certification.getCode());
            row.createCell(1).setCellValue(certification.getType());
            row.createCell(2).setCellValue(certification.getConsultation().getCodeConsultation());
            row.createCell(3).setCellValue(certification.getDateDebut().toString());
            row.createCell(4).setCellValue(certification.getDateFin().toString());
        }

        // Écriture du workbook dans un ByteArrayOutputStream et retour des données en tableau d'octets
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            workbook.write(byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        }
    }
}
