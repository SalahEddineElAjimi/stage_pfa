package com.amasoft.amaclinic.Service.Impl;

import com.amasoft.amaclinic.Entity.Cabinet;
import com.amasoft.amaclinic.Entity.Centre;
import com.amasoft.amaclinic.Entity.Salle;
import com.amasoft.amaclinic.Service.CabinetService;
import com.amasoft.amaclinic.Service.CentreService;
import com.amasoft.amaclinic.Service.SalleService;
import com.amasoft.amaclinic.criteria.CabinetCriteria;
import com.amasoft.amaclinic.dto.request.CabinetRequestDTO;
import com.amasoft.amaclinic.dto.response.CabinetResponseDTO;
import com.amasoft.amaclinic.exception.EntityAlreadyExisteException;
import com.amasoft.amaclinic.exception.EntityNotFoundException;
import com.amasoft.amaclinic.mapper.CabinetMapper;
import com.amasoft.amaclinic.repository.CabinetRepository;
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
public class CabinetServiceImpl implements CabinetService {

    private final CabinetRepository cabinetRepo;
    private final CabinetMapper cabinetMapper;
    private final CentreService centreService;

    @Override
    public Page<CabinetResponseDTO> trouverCabinetsParCritere(CabinetCriteria cabinetCriteria, int page, int size) {
        PageRequest pageable = PageRequest.of(page, size);
        Page<Cabinet> cabinetPage = cabinetRepo.findAll((root, query, criteriaBuilder) -> {
            List<Predicate> predicateList = new ArrayList<>();
            if (cabinetCriteria.getId() != null) {
                predicateList.add(criteriaBuilder.equal(root.get("id"), cabinetCriteria.getId()));
            }
            if (cabinetCriteria.getCodeCabinet() != null) {
                predicateList.add(criteriaBuilder.equal(root.get("codeCabinet"), cabinetCriteria.getCodeCabinet()));
            }
            if (cabinetCriteria.getNomCabinet() != null) {
                predicateList.add(criteriaBuilder.equal(root.get("nomCabinet"), cabinetCriteria.getNomCabinet()));
            }
            if (cabinetCriteria.getAdresse() != null) {
                predicateList.add(criteriaBuilder.equal(root.get("adresse"), cabinetCriteria.getAdresse()));
            }
            return criteriaBuilder.and(predicateList.toArray(new Predicate[0]));
        }, pageable);
        return cabinetMapper.modelToDtos(cabinetPage);
    }

    @Override
    public CabinetResponseDTO ajouterCabinet(CabinetRequestDTO cabinetRequestDTO) {
        // Generate a unique code for the Cabinet
        String generatedCode = "CAB" + UUID.randomUUID().toString().replace("-", "").substring(0, 8);
        cabinetRequestDTO.setCodeCabinet(generatedCode);

        Optional<Cabinet> existingCabinet = cabinetRepo.findByCodeCabinet(generatedCode);
        if (existingCabinet.isPresent()) {
            throw new EntityAlreadyExisteException("Cabinet déjà existant avec le Code Cabinet : " + generatedCode);
        }

        Cabinet cabinetToSave = cabinetMapper.dtoToModel(cabinetRequestDTO);

        if (cabinetRequestDTO.getCodeCentres() != null && !cabinetRequestDTO.getCodeCentres().isEmpty()) {
            List<Centre> centres = centreService.findByCodes(cabinetRequestDTO.getCodeCentres());
            cabinetToSave.setCentres((List<Centre>) new HashSet<>(centres));
        }

        Cabinet savedCabinet = cabinetRepo.save(cabinetToSave);
        return cabinetMapper.modelToDto(savedCabinet);
    }


    @Override
    public CabinetResponseDTO modifierCabinet(CabinetRequestDTO cabinetRequestDTO) {
        Optional<Cabinet> existingCabinet = cabinetRepo.findByCodeCabinet(cabinetRequestDTO.getCodeCabinet());
        if (existingCabinet.isEmpty()) {
            throw new EntityNotFoundException("Cabinet non trouvé avec le Code Cabinet : " + cabinetRequestDTO.getCodeCabinet());
        }

        Cabinet cabinetToUpdate = cabinetMapper.dtoToModel(cabinetRequestDTO);
        cabinetToUpdate.setId(existingCabinet.get().getId());

        if (cabinetRequestDTO.getCodeCentres() != null && !cabinetRequestDTO.getCodeCentres().isEmpty()) {
            List<Centre> centres = centreService.findByCodes(cabinetRequestDTO.getCodeCentres());
            cabinetToUpdate.setCentres((List<Centre>) new HashSet<>(centres));
        }

        Cabinet updatedCabinet = cabinetRepo.save(cabinetToUpdate);
        return cabinetMapper.modelToDto(updatedCabinet);
    }

    @Override
    public void supprimerCabinet(String codeCabinet) {
        Optional<Cabinet> cabinet = cabinetRepo.findByCodeCabinet(codeCabinet);
        if (cabinet.isEmpty()) {
            throw new EntityNotFoundException("Cabinet non trouvé avec code : " + codeCabinet);
        }


        cabinetRepo.deleteByCodeCabinet(codeCabinet);
    }

    @Override
    public CabinetResponseDTO trouverCabinetParCodeCabinet(String codeCabinet) {
        Optional<Cabinet> cabinetOptional = cabinetRepo.findByCodeCabinet(codeCabinet);
        return cabinetOptional.map(cabinetMapper::modelToDto).orElse(null);
    }

    @Override
    public byte[] generateCabinetExcel() throws IOException {
        List<Cabinet> cabinets = cabinetRepo.findAll();

        // Création d'un nouveau workbook et d'une feuille
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Cabinets");

        // Création de la ligne d'en-tête
        Row headerRow = sheet.createRow(0);
        String[] headers = {
                "Code Cabinet",
                "Nom Cabinet",
                "Adresse",
                "Salle Code",
                "Salle Nom",
                "Salle Type",
                "Centre Code",
                "Centre Nom",
                "Centre Adresse"
        };

        // Remplissage de la ligne d'en-tête
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }

        int rowNum = 1;
        // Remplissage des données
        for (Cabinet cabinet : cabinets) {
            // Récupération des détails du cabinet
            String codeCabinet = cabinet.getCodeCabinet();
            String nomCabinet = cabinet.getNomCabinet();
            String adresse = cabinet.getAdresse();

            // Remplissage des données du cabinet
            for (Salle salle : cabinet.getSalles()) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(codeCabinet);
                row.createCell(1).setCellValue(nomCabinet);
                row.createCell(2).setCellValue(adresse);
                row.createCell(3).setCellValue(salle.getCodeSalle());
                row.createCell(4).setCellValue(salle.getNomSalle());
                row.createCell(5).setCellValue(salle.getType());

                // Les informations sur les centres sont ajoutées ici, si disponibles
                for (Centre centre : cabinet.getCentres()) {
                    row.createCell(6).setCellValue(centre.getCodeCentre());
                    row.createCell(7).setCellValue(centre.getNomCentre());
                    row.createCell(8).setCellValue(centre.getAdresse());
                }
            }
        }

        // Écriture du workbook dans un ByteArrayOutputStream et retour des données en tableau d'octets
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            workbook.write(byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        }
    }
}
