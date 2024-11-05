package com.amasoft.amaclinic.Service.Impl;

import com.amasoft.amaclinic.Entity.Secretaire;
import com.amasoft.amaclinic.Service.SecretaireService;
import com.amasoft.amaclinic.criteria.secretaireCriteria;
import com.amasoft.amaclinic.dto.request.secretaireRequestDto;
import com.amasoft.amaclinic.dto.response.responseSecretaire;
import com.amasoft.amaclinic.exception.EntityAlreadyExisteException;
import com.amasoft.amaclinic.exception.EntityNotFoundException;
import com.amasoft.amaclinic.mapper.SecretaireMapper;
import com.amasoft.amaclinic.repository.SecretaireRepository;
import lombok.AllArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.rowset.Predicate;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
@Transactional
public class SecretaireServiceImpl implements SecretaireService {

    private SecretaireRepository secretaireRepository;
    private SecretaireMapper secretaireMapper;

    @Override
    public Page<responseSecretaire> trouverSecretaireByCriteria(secretaireCriteria secretaireCriteria, int page, int size) {
        PageRequest pageable = PageRequest.of(page, size);
        Page<Secretaire> secretairePage = secretaireRepository.findAll((root, query, criteriaBuilder) ->
        {
            List<Predicate> predicateList = new ArrayList<>();
            if (secretaireCriteria.getId() != null)
            {predicateList.add((Predicate) criteriaBuilder.equal(root.get("id"),secretaireCriteria.getId())); }
            if (secretaireCriteria.getNom()!=null)
            {predicateList.add((Predicate) criteriaBuilder.equal(root.get("nom"),secretaireCriteria.getNom()));}
            if (secretaireCriteria.getCodeSecretaire()!=null)
            {predicateList.add((Predicate) criteriaBuilder.equal(root.get("CodeSecretaire"),secretaireCriteria.getCodeSecretaire()));}
            if(secretaireCriteria.getPrenom()!=null)
            {predicateList.add((Predicate) criteriaBuilder.equal(root.get("prenom"),secretaireCriteria.getPrenom()));}
            return criteriaBuilder.and(predicateList.toArray(new jakarta.persistence.criteria.Predicate[0]));
        } , pageable);
        return secretaireMapper.modelToDtos(secretairePage);
    }

    @Override
    public responseSecretaire addSecretaire(secretaireRequestDto secretaire) {
        String generatedCode_secretaire = "SCR" + UUID.randomUUID().toString().replace("-" ,"").substring(0,8);
        Secretaire secretaireToSave = secretaireMapper.dtoToModel(secretaire);
        secretaireToSave.setCodeSecretaire(generatedCode_secretaire);
        Optional<Secretaire> existingSecretaire = secretaireRepository.findByCodeSecretaire(secretaireToSave.getCodeSecretaire());
        if (existingSecretaire.isPresent())
        {
            throw new EntityAlreadyExisteException("le secretaire existe deja avec le code :" +secretaire.getCodeSecretaire());
        }
        com.amasoft.amaclinic.Entity.Secretaire savedsecretaire = secretaireRepository.save(secretaireToSave);
        return secretaireMapper.modelToDto(savedsecretaire);
    }

    @Override
    public void deleteSecretaire(String CodeSecretaire) {
        Optional<Secretaire> secretaire = secretaireRepository.findByCodeSecretaire(CodeSecretaire);
        if(secretaire.isEmpty())
        {
            throw new EntityNotFoundException("secretaire n'existe pas code :" +CodeSecretaire);
        }
        secretaireRepository.deleteByCodeSecretaire(CodeSecretaire);
    }

    @Override
    public responseSecretaire updateSecretaire(secretaireRequestDto secretaire) {
        Optional<Secretaire> existingSecretaire = secretaireRepository.findByCodeSecretaire(secretaire.getCodeSecretaire());
        if (existingSecretaire.isEmpty())
        {
            throw new EntityNotFoundException("secretaire n'existe pas ");
        }
        Secretaire secretaireToUp = secretaireMapper.dtoToModel(secretaire);
        secretaireToUp.setId(existingSecretaire.get().getId());
        secretaireToUp.setCodeSecretaire(existingSecretaire.get().getCodeSecretaire());
        com.amasoft.amaclinic.Entity.Secretaire updatedSec = secretaireRepository.save(secretaireToUp);

        return secretaireMapper.modelToDto(updatedSec);
    }

    @Override
    public byte[] generateSercretaireExcel() throws IOException {
        List<Secretaire> secretaires = secretaireRepository.findAll();
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Secretaire");
        Row headerRow = sheet.createRow(0);
        String[] headers = {
                "ID",
                "Code Secretaire",
                "Nom",
                "Prenom",
                "Adresse",
                "Telephone"
        };

        // Création de l'en-tête
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }

        int rowNum = 1;
        // Remplissage des données
        for (Secretaire secretaire : secretaires) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(secretaire.getId());
            row.createCell(1).setCellValue(secretaire.getCodeSecretaire());
            row.createCell(2).setCellValue(secretaire.getNom());
            row.createCell(3).setCellValue(secretaire.getPrenom());
            row.createCell(4).setCellValue(secretaire.getAdresse());
            row.createCell(5).setCellValue(secretaire.getTelephone());
        }

        // Écriture du fichier Excel en mémoire et retour en tableau d'octets
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            workbook.write(byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        }
    }
}
