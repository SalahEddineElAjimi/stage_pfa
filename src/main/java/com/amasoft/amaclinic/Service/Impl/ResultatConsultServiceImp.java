package com.amasoft.amaclinic.Service.Impl;

import com.amasoft.amaclinic.Entity.Consultation;
import com.amasoft.amaclinic.Entity.ResultatConsultation;
import com.amasoft.amaclinic.Service.ResultatConsultService;
import com.amasoft.amaclinic.criteria.ResultatConsltCriteria;
import com.amasoft.amaclinic.dto.request.ResultatConsltRequestDTO;
import com.amasoft.amaclinic.dto.response.ResultatConsltResponseDTO;
import com.amasoft.amaclinic.exception.EntityAlreadyExisteException;
import com.amasoft.amaclinic.exception.EntityNotFoundException;
import com.amasoft.amaclinic.mapper.ResultatConsultMapper;
import com.amasoft.amaclinic.repository.ConsultationRepository;
import com.amasoft.amaclinic.repository.ResultatConsultRepository;
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
public class ResultatConsultServiceImp implements ResultatConsultService {
    private ResultatConsultRepository resultatConsultRepository;
    private ConsultationRepository consultationRepository;
    private ResultatConsultMapper resultatConsultMapper;

    @Override
    public ResultatConsltResponseDTO addResultatConsultation(ResultatConsltRequestDTO resultatConsltRequestDTO) {
        String generatedCodeResultat = "RSC" + UUID.randomUUID().toString().replace("-", "").substring(0, 8);
        ResultatConsultation resultatToSave = resultatConsultMapper.dtoToModel(resultatConsltRequestDTO);
        resultatToSave.setCodeResultatConsultation(generatedCodeResultat);
        Optional<Consultation> consultation = consultationRepository.findConsultationByCodeConsultation(resultatConsltRequestDTO.getCodeConsultation());
        if (consultation.isEmpty()) {
            throw new EntityNotFoundException("Aucunne consultation trouvé avec le code : " + resultatConsltRequestDTO.getCodeConsultation());
        }
        resultatToSave.setConsultation(consultation.get());
        Optional<ResultatConsultation> existingResultat = resultatConsultRepository.findResultatConsultByCodeResultatConsultation(resultatToSave.getCodeResultatConsultation());
        if (existingResultat.isPresent()) {
            throw new EntityAlreadyExisteException("resultat already exists with id: " + resultatConsltRequestDTO.getId());
        }
        ResultatConsultation savedResultat = resultatConsultRepository.save(resultatToSave);
        return resultatConsultMapper.modelToDto(savedResultat);
    }

    @Override
    public ResultatConsltResponseDTO updateResultatConsultation(ResultatConsltRequestDTO resultatConsltRequestDTO) {
        Optional<ResultatConsultation> existingResultat = resultatConsultRepository.findResultatConsultByCodeResultatConsultation(resultatConsltRequestDTO.getCodeResultatConsultation());

        if (existingResultat.isEmpty()) {
            throw new EntityNotFoundException("Aucun resultat trouvé avec le code : " + resultatConsltRequestDTO.getCodeResultatConsultation());
        }

        ResultatConsultation resultatToUpdate = resultatConsultMapper.dtoToModel(resultatConsltRequestDTO);
        resultatToUpdate.setId(existingResultat.get().getId());

        Optional<Consultation> consultation = consultationRepository.findConsultationByCodeConsultation(resultatConsltRequestDTO.getCodeConsultation());
        if (consultation.isEmpty()) {
            throw new EntityNotFoundException("Aucune consultation trouvée avec le code : " + resultatConsltRequestDTO.getCodeConsultation());
        }
        resultatToUpdate.setConsultation(consultation.get());

        ResultatConsultation updatedResultat = resultatConsultRepository.save(resultatToUpdate);
        return resultatConsultMapper.modelToDto(updatedResultat);
    }

    @Override
    public void deleteResultatConsultation(String codeResultatConsultation) {
        Optional<ResultatConsultation> resultatConsultation = resultatConsultRepository.findResultatConsultByCodeResultatConsultation(codeResultatConsultation);
        if (resultatConsultation.isEmpty()){
            throw new EntityNotFoundException("resultat not found :" +codeResultatConsultation);
        }
        resultatConsultRepository.deleteByCodeResultatConsultation(codeResultatConsultation);
    }

    @Override
    public Page<ResultatConsltResponseDTO> findResultatConsultationByCriteria(ResultatConsltCriteria resultatConsltCriteria, int page, int size) {
        PageRequest pageable = PageRequest.of(page, size);

        Page<ResultatConsultation> resultatConsultationPage = resultatConsultRepository.findAll((root, query, criteriaBuilder) -> {
            List<Predicate> predicateList = new ArrayList<>();

            if (resultatConsltCriteria.getId() != null) {
                predicateList.add(criteriaBuilder.equal(root.get("id"), resultatConsltCriteria.getId()));
            }
            if (resultatConsltCriteria.getResultaConsultation() != null) {
                predicateList.add(criteriaBuilder.equal(root.get("codeResultatConsultation"), resultatConsltCriteria.getResultaConsultation()));
            }
            if (resultatConsltCriteria.getDescription() != null) {
                predicateList.add(criteriaBuilder.equal(root.get("description"), resultatConsltCriteria.getDescription()));
            }
            if (resultatConsltCriteria.getCodeConsultation() !=null){
                predicateList.add(criteriaBuilder.equal(root.get("codeConsultation"), resultatConsltCriteria.getCodeConsultation()));
            }


            return criteriaBuilder.and(predicateList.toArray(new Predicate[0]));
        }, pageable);

        return resultatConsultMapper.modelToDtos(resultatConsultationPage);
    }

    @Override
    public byte[] generateResultatExcel() throws IOException {
        List<ResultatConsultation> resultats = resultatConsultRepository.findAll();
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("ResultatsConsultation");
        Row headerRow = sheet.createRow(0);
        String[] headers = {"ID", "Code Résultat Consultation", "Motif", "Description", "Code Consultation"};

        // Création de l'en-tête
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }

        int rowNum = 1;
        // Remplissage des données
        for (ResultatConsultation resultat : resultats) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(resultat.getId());
            row.createCell(1).setCellValue(resultat.getCodeResultatConsultation());
            row.createCell(2).setCellValue(resultat.getMotif());
            row.createCell(3).setCellValue(resultat.getDescription());
            row.createCell(4).setCellValue(resultat.getCodeConsultation());
        }

        // Écriture du fichier Excel en mémoire et retour en tableau d'octets
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            workbook.write(byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        }
    }
}
