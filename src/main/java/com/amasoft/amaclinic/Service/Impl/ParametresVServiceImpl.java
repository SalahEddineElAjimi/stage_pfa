package com.amasoft.amaclinic.Service.Impl;


import com.amasoft.amaclinic.Entity.ParametresVitaux;
import com.amasoft.amaclinic.Entity.Patient;
import com.amasoft.amaclinic.Service.ParametresVService;
import com.amasoft.amaclinic.criteria.ParametresVCriteria;
import com.amasoft.amaclinic.dto.request.ParametresVRequestDto;
import com.amasoft.amaclinic.dto.response.ParametresVResponseDto;
import com.amasoft.amaclinic.exception.EntityAlreadyExisteException;
import com.amasoft.amaclinic.exception.EntityNotFoundException;
import com.amasoft.amaclinic.mapper.ParametresVMapper;
import com.amasoft.amaclinic.repository.ParametresVRepository;
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
public class ParametresVServiceImpl implements ParametresVService {
    private ParametresVRepository parametresVRepository;
    private PatientRepository patientRepo;
    private ParametresVMapper parametresVMapper;

    @Override
    public ParametresVResponseDto addParametres(ParametresVRequestDto parametresVRequestDto) {
        String generatedCodeParametre = "PRMV" + UUID.randomUUID().toString().replace("-", "").substring(0, 8);
        ParametresVitaux parametreToSave = parametresVMapper.dtoToModel(parametresVRequestDto);
        parametreToSave.setCodeParametre(generatedCodeParametre);

        Optional<Patient>  patient = patientRepo.findPatientByCodePatient(parametresVRequestDto.getPatientCode());
        parametreToSave.setPatient(patient.get());

        Optional<ParametresVitaux> existingParam = parametresVRepository.findParametreByCodeParametre(parametreToSave.getCodeParametre());
        if (existingParam.isPresent()) {
            throw new EntityAlreadyExisteException("Parametre already exists with id: " + parametresVRequestDto.getId());
        }
        ParametresVitaux savedParametre = parametresVRepository.save(parametreToSave);
        return parametresVMapper.modelToDto(savedParametre);
    }

    @Override
    public ParametresVResponseDto updateParametres(ParametresVRequestDto parametresVRequestDto) {
        Optional<ParametresVitaux> existingParametre = parametresVRepository.findParametreByCodeParametre(parametresVRequestDto.getCodeParametre());
        if (existingParametre.isEmpty()){
            throw new EntityNotFoundException("Parametre non trouvé ");
        }
        ParametresVitaux parametreToUpdate = parametresVMapper.dtoToModel(parametresVRequestDto);
        parametreToUpdate.setId(existingParametre.get().getId());
        parametreToUpdate.setCodeParametre(existingParametre.get().getCodeParametre());

        parametreToUpdate.setPatient(patientRepo.findPatientByCodePatient(parametresVRequestDto.getPatientCode()).get());
        ParametresVitaux updatedParametre= parametresVRepository.save(parametreToUpdate);
        return parametresVMapper.modelToDto(updatedParametre);
    }

    @Override
    public void deleteParametres(String codeParametre) {
        Optional<ParametresVitaux> parametresVitaux = parametresVRepository.findParametreByCodeParametre(codeParametre);
        if (parametresVitaux.isEmpty()){
            throw new EntityNotFoundException("Parametre non trouvé avec le code :  "+codeParametre);
        }

        parametresVRepository.deleteByCodeParametre(codeParametre);
    }


    @Override
    public Page<ParametresVResponseDto> findParametreByCriteria(ParametresVCriteria parametresVCriteria, int page, int size) {
        PageRequest pageable = PageRequest.of(page, size);
        Page<ParametresVitaux> parametresVitauxPage = parametresVRepository.findAll((root, query, criteriaBuilder) -> {
            List<Predicate> predicateList = new ArrayList<>();
            if (parametresVCriteria.getId() != null){
                predicateList.add(criteriaBuilder.equal(root.get("id"),parametresVCriteria.getId()));
            }
            if (parametresVCriteria.getPoids() != null){
                predicateList.add(criteriaBuilder.equal(root.get("poids"),parametresVCriteria.getPoids()));
            }

            if (parametresVCriteria.getHauteur() != null){
                predicateList.add(criteriaBuilder.equal(root.get("hauteur"),parametresVCriteria.getHauteur()));
            }

            if (parametresVCriteria.getPatientCode() != null){
                predicateList.add(criteriaBuilder.equal(root.get("parametreCode"),parametresVCriteria.getParametreCode()));
            }

            return criteriaBuilder.and(predicateList.toArray(new Predicate[0]));

        } , pageable);
        return parametresVMapper.modelToDtos(parametresVitauxPage);
    }

    @Override
    public byte[] generateParametreV() throws IOException {
        List<ParametresVitaux> parametres = parametresVRepository.findAll();
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Parametre");
        Row headerRow = sheet.createRow(0);
        String[] headers = {
                "ID",
                "Code Parametre",
                "Poids",
                "Hauteur",
                "Température Corporelle",
                "Rythme Cardiaque",
                "Fréquence Respiratoire",
                "Pression Artérielle",
                "Code Patient"
        };

        // Création de l'en-tête
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }

        int rowNum = 1;
        // Remplissage des données
        for (ParametresVitaux parametre : parametres) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(parametre.getId());
            row.createCell(1).setCellValue(parametre.getCodeParametre());
            row.createCell(2).setCellValue(parametre.getPoids());
            row.createCell(3).setCellValue(parametre.getHauteur());
            row.createCell(4).setCellValue(parametre.getTemperatureCorporelle());
            row.createCell(5).setCellValue(parametre.getRythmCardiaque());
            row.createCell(6).setCellValue(parametre.getFrequenceRespiratoire());
            row.createCell(7).setCellValue(parametre.getPressionArterielle());
            row.createCell(8).setCellValue(parametre.getPatientCode());
        }

        // Écriture du fichier Excel en mémoire et retour en tableau d'octets
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            workbook.write(byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        }
    }
}
