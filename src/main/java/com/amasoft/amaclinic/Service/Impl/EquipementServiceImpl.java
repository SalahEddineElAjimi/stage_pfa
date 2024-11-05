package com.amasoft.amaclinic.Service.Impl;

import com.amasoft.amaclinic.Entity.Equipement;
import com.amasoft.amaclinic.Entity.Salle;
import com.amasoft.amaclinic.Service.EquipementService;
import com.amasoft.amaclinic.Service.SalleService;
import com.amasoft.amaclinic.criteria.EquipementCriteria;
import com.amasoft.amaclinic.dto.request.EquipementRequestDTO;
import com.amasoft.amaclinic.dto.response.EquipementResponseDTO;
import com.amasoft.amaclinic.exception.EntityAlreadyExisteException;
import com.amasoft.amaclinic.exception.EntityNotFoundException;
import com.amasoft.amaclinic.mapper.EquipementMapper;
import com.amasoft.amaclinic.repository.EquipementRepository;
import com.amasoft.amaclinic.repository.SalleRepository;
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
import java.util.*;

@Service
@AllArgsConstructor
@Transactional
public class EquipementServiceImpl implements EquipementService {

    private final EquipementRepository equipementRepo;
    private final EquipementMapper equipementMapper;
    private final SalleService salleService;
    private final SalleRepository salleRepository;

    @Override
    public Page<EquipementResponseDTO> trouverEquipementsParCritere(EquipementCriteria equipementCriteria, int page, int size) {
        PageRequest pageable = PageRequest.of(page, size);
        Page<Equipement> equipementPage = equipementRepo.findAll((root, query, criteriaBuilder) -> {
            List<Predicate> predicateList = new ArrayList<>();
            if (equipementCriteria.getCodeEquipement() != null) {
                predicateList.add(criteriaBuilder.equal(root.get("codeEquipement"), equipementCriteria.getCodeEquipement()));
            }
            if (equipementCriteria.getNomEquipement() != null) {
                predicateList.add(criteriaBuilder.equal(root.get("nomEquipement"), equipementCriteria.getNomEquipement()));
            }
            if (equipementCriteria.getSalleId() != null) {
                predicateList.add(criteriaBuilder.equal(root.join("salles").get("idSalle"), equipementCriteria.getSalleId()));
            }
            return criteriaBuilder.and(predicateList.toArray(new Predicate[0]));
        }, pageable);
        return equipementMapper.toDtoPage(equipementPage);
    }

    @Override
    @Transactional
    public EquipementResponseDTO ajouterEquipement(EquipementRequestDTO requestDTO) {
        String generatedCodeEquipement = "EQP" + UUID.randomUUID().toString().replace("-", "").substring(0, 8);

        Equipement equipement = equipementMapper.toEntity(requestDTO);
        equipement.setCodeEquipement(generatedCodeEquipement);

        equipement = equipementRepo.save(equipement);

        return equipementMapper.toDto(equipement);
    }

    @Override
    public EquipementResponseDTO modifierEquipement(EquipementRequestDTO equipementRequestDTO) {
        Equipement equipement = equipementRepo.findByCodeEquipement(equipementRequestDTO.getCodeEquipement())
                .orElseThrow(() -> new EntityNotFoundException("Équipement non trouvé avec le code : " + equipementRequestDTO.getCodeEquipement()));

        equipement = equipementMapper.toEntity(equipementRequestDTO, equipement);

        Equipement updatedEquipement = equipementRepo.save(equipement);
        return equipementMapper.toDto(updatedEquipement);
    }

    @Override
    public void supprimerEquipement(String codeEquipement) {
        equipementRepo.deleteByCodeEquipement(codeEquipement);
    }


    @Override
    public byte[] generateEquipementExcel() throws IOException {
        List<Equipement> equipements = equipementRepo.findAll();

        // Création d'un nouveau workbook et d'une feuille
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Equipements");

        // Création de la ligne d'en-tête
        Row headerRow = sheet.createRow(0);
        String[] headers = {
                "Code Equipement",
                "Nom Equipement",
                "Type Equipement",
                "Codes Salles"
        };

        // Remplissage de la ligne d'en-tête
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }

        int rowNum = 1;
        // Remplissage des données
        for (Equipement equipement : equipements) {
            // Récupération des détails de l'équipement
            String codeEquipement = equipement.getCodeEquipement();
            String nomEquipement = equipement.getNomEquipement();
            String typeEquipement = equipement.getTypeEquipement();

            // Remplissage des données de l'équipement
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(codeEquipement);
            row.createCell(1).setCellValue(nomEquipement);
            row.createCell(2).setCellValue(typeEquipement);

            /* Ajout des codes des salles associées
            String salleCodes = String.join(", ", equipement.getSalleCodes());
            row.createCell(3).setCellValue(salleCodes);*/
        }

        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            workbook.write(byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        }
    }
}
