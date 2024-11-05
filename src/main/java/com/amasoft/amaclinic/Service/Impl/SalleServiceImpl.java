package com.amasoft.amaclinic.Service.Impl;

import com.amasoft.amaclinic.Entity.*;
import com.amasoft.amaclinic.Service.CabinetService;
import com.amasoft.amaclinic.Service.SalleService;
import com.amasoft.amaclinic.criteria.SalleCriteria;
import com.amasoft.amaclinic.dto.request.SalleRequestDTO;
import com.amasoft.amaclinic.dto.response.CabinetResponseDTO;
import com.amasoft.amaclinic.dto.response.SalleResponceDTO;
import com.amasoft.amaclinic.exception.EntityAlreadyExisteException;
import com.amasoft.amaclinic.exception.EntityNotFoundException;
import com.amasoft.amaclinic.mapper.SalleMapper;
import com.amasoft.amaclinic.repository.CabinetRepository;
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
public class SalleServiceImpl implements SalleService {

    private final SalleRepository salleRepo;
    private final SalleMapper salleMapper;
    private final CabinetService cabinetService;
    private final EquipementRepository equipementRepository;

    private final CabinetRepository cabinetRepository;

    @Override
    public Page<SalleResponceDTO> trouverSallesParCritere(SalleCriteria salleCriteria, int page, int size) {
        PageRequest pageable = PageRequest.of(page, size);
        Page<Salle> sallePage = salleRepo.findAll((root, query, criteriaBuilder) -> {
            List<Predicate> predicateList = new ArrayList<>();
            if (salleCriteria.getId() != null) {
                predicateList.add(criteriaBuilder.equal(root.get("idSalle"), salleCriteria.getId()));
            }
            if (salleCriteria.getCodeSalle() != null) {
                predicateList.add(criteriaBuilder.equal(root.get("codeSalle"), salleCriteria.getCodeSalle()));
            }
            if (salleCriteria.getNomSalle() != null) {
                predicateList.add(criteriaBuilder.equal(root.get("nomSalle"), salleCriteria.getNomSalle()));
            }
            if (salleCriteria.getType() != null) {
                predicateList.add(criteriaBuilder.equal(root.get("type"), salleCriteria.getType()));
            }
            if (salleCriteria.getCodeCabinet() != null) {
                predicateList.add(criteriaBuilder.equal(root.get("cabinet").get("codeCabinet"), salleCriteria.getCodeCabinet()));
            }
            return criteriaBuilder.and(predicateList.toArray(new Predicate[0]));
        }, pageable);
        return salleMapper.modelToDtos(sallePage);
    }

    @Override
    public SalleResponceDTO ajouterSalle(SalleRequestDTO salleRequestDTO) {
        String generatedCodeSalle = "SL" + UUID.randomUUID().toString().replace("-", "").substring(0, 8);
        Salle salleTosave = salleMapper.dtoToModel(salleRequestDTO);
        salleTosave.setCodeSalle(generatedCodeSalle);

        Optional<Cabinet> cabinet = cabinetRepository.findByCodeCabinet(salleRequestDTO.getCodeCabinet());
        salleTosave.setCabinet(cabinet.orElseThrow(() -> new EntityNotFoundException("Cabinet non trouvé")));

        List<Equipement> equipements = equipementRepository.findByCodeEquipementIn(salleRequestDTO.getCodeEquipements());
        salleTosave.setEquipements(new HashSet<>(equipements));

        Optional<Salle> existingSalle = salleRepo.findByCodeSalle(salleTosave.getCodeSalle());
        if (existingSalle.isPresent()) {
            throw new EntityAlreadyExisteException("Salle existe déjà avec code: " + salleTosave.getCodeSalle());
        }

        Salle savedSalle = salleRepo.save(salleTosave);
        return salleMapper.modelToDto(savedSalle);
    }





    @Override
    public SalleResponceDTO modifierSalle(SalleRequestDTO salleRequestDTO) {

        Optional<Salle> existingSalleOpt = salleRepo.findByCodeSalle(salleRequestDTO.getCodeSalle());
        if (existingSalleOpt.isEmpty()) {
            throw new EntityNotFoundException("Salle non trouvée avec code : " + salleRequestDTO.getCodeSalle());
        }

        Salle salleToUpdate = salleMapper.dtoToModel(salleRequestDTO);

        Salle existingSalle = existingSalleOpt.get();
        salleToUpdate.setIdSalle(existingSalle.getIdSalle());
        salleToUpdate.setCodeSalle(existingSalle.getCodeSalle());

        Cabinet cabinet = cabinetRepository.findByCodeCabinet(salleRequestDTO.getCodeCabinet())
                .orElseThrow(() -> new EntityNotFoundException("Cabinet non trouvé"));
        salleToUpdate.setCabinet(cabinet);

        List<Equipement> equipements = equipementRepository.findByCodeEquipementIn(salleRequestDTO.getCodeEquipements());
        salleToUpdate.setEquipements(new HashSet<>(equipements));

        Salle updatedSalle = salleRepo.save(salleToUpdate);

        return salleMapper.modelToDto(updatedSalle);
    }



    @Override
    public void supprimerSalle(String codeSalle) {
        if (!salleRepo.existsByCodeSalle(codeSalle)) {
            throw new EntityNotFoundException("Salle non trouvée avec code : " + codeSalle);
        }
        salleRepo.deleteByCodeSalle(codeSalle);
    }

    private SalleResponceDTO convertToResponseDTO(Salle salle) {
        return SalleResponceDTO.builder()
                .codeSalle(salle.getCodeSalle())
                .nomSalle(salle.getNomSalle())
                .type(salle.getType())
                .codeCabinet(salle.getCabinet().getCodeCabinet())
                .build();
    }

    @Override

    public List<Salle> findByCodes(List<String> codes) {
        return salleRepo.findByCodeSalleIn(codes);
    }

    @Override
    public Salle save(Salle salle) {
        return salleRepo.save(salle);
    }

    public byte[] generateSalleExcel() throws IOException {
        List<Salle> salles = salleRepo.findAll();

        // Création d'un nouveau workbook et d'une feuille
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Salles");

        // Création de la ligne d'en-tête
        Row headerRow = sheet.createRow(0);
        String[] headers = {
                "Code Salle",
                "Nom Salle",
                "Type",
                "Code Cabinet"
        };

        // Remplissage de la ligne d'en-tête
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }

        int rowNum = 1;
        // Remplissage des données
        for (Salle salle : salles) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(salle.getCodeSalle());
            row.createCell(1).setCellValue(salle.getNomSalle());
            row.createCell(2).setCellValue(salle.getType());
            row.createCell(3).setCellValue(salle.getCabinet().getCodeCabinet());
        }

        // Écriture du workbook dans un ByteArrayOutputStream et retour des données en tableau d'octets
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            workbook.write(byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        }
    }
}
