package com.amasoft.amaclinic.Service.Impl;

import com.amasoft.amaclinic.Entity.Centre;
import com.amasoft.amaclinic.Entity.Cabinet;
import com.amasoft.amaclinic.Service.CentreService;
import com.amasoft.amaclinic.criteria.CentreCriteria;
import com.amasoft.amaclinic.dto.request.CentreRequestDTO;
import com.amasoft.amaclinic.dto.response.CentreResponseDTO;
import com.amasoft.amaclinic.exception.EntityAlreadyExisteException;
import com.amasoft.amaclinic.exception.EntityNotFoundException;
import com.amasoft.amaclinic.mapper.CentreMapper;
import com.amasoft.amaclinic.repository.CentreRepository;
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
public class CentreServiceImpl implements CentreService {

    private final CentreRepository centreRepo;
    private final CentreMapper centreMapper;

    @Override
    public Page<CentreResponseDTO> trouverCentresParCritere(CentreCriteria centreCriteria, int page, int size) {
        PageRequest pageable = PageRequest.of(page, size);
        Page<Centre> centrePage = centreRepo.findAll((root, query, criteriaBuilder) -> {
            List<Predicate> predicateList = new ArrayList<>();
            if (centreCriteria.getId() != null) {
                predicateList.add(criteriaBuilder.equal(root.get("id"), centreCriteria.getId()));
            }
            if (centreCriteria.getCodeCentre() != null) {
                predicateList.add(criteriaBuilder.equal(root.get("codeCentre"), centreCriteria.getCodeCentre()));
            }
            if (centreCriteria.getNomCentre() != null) {
                predicateList.add(criteriaBuilder.equal(root.get("nomCentre"), centreCriteria.getNomCentre()));
            }
            if (centreCriteria.getAdresse() != null) {
                predicateList.add(criteriaBuilder.equal(root.get("adresse"), centreCriteria.getAdresse()));
            }
            if (centreCriteria.getTypeCentre() != null) {
                predicateList.add(criteriaBuilder.equal(root.get("typeCentre"), centreCriteria.getTypeCentre()));
            }
            if (centreCriteria.getTelephone() != null) {
                predicateList.add(criteriaBuilder.equal(root.get("telephone"), centreCriteria.getTelephone()));
            }
            return criteriaBuilder.and(predicateList.toArray(new Predicate[0]));
        }, pageable);
        return centreMapper.modelToDtos(centrePage);
    }

    @Override
    public CentreResponseDTO ajouterCentre(CentreRequestDTO centreRequestDTO) {
        // Generate a unique code for the Centre
        String generatedCode = "CENTRE" + UUID.randomUUID().toString().replace("-", "").substring(0, 8);
        centreRequestDTO.setCodeCentre(generatedCode);

        Optional<Centre> existingCentre = centreRepo.findByNomCentre(centreRequestDTO.getNomCentre());
        if (existingCentre.isPresent()) {
            throw new EntityAlreadyExisteException("Centre déjà existant avec le nom : " + centreRequestDTO.getNomCentre());
        }

        Centre centreToSave = centreMapper.dtoToModel(centreRequestDTO);
        Centre savedCentre = centreRepo.save(centreToSave);
        return centreMapper.modelToDto(savedCentre);
    }


    @Override
    public CentreResponseDTO modifierCentre(CentreRequestDTO centreRequestDTO) {
        Optional<Centre> existingCentre = centreRepo.findByCodeCentre(centreRequestDTO.getCodeCentre());
        if (existingCentre.isEmpty()) {
            throw new EntityNotFoundException("Centre non trouvé avec code centre : " + centreRequestDTO.getCodeCentre());
        }

        Centre centreToUpdate = centreMapper.dtoToModel(centreRequestDTO);
        centreToUpdate.setId(existingCentre.get().getId());
        Centre updatedCentre = centreRepo.save(centreToUpdate);
        return centreMapper.modelToDto(updatedCentre);
    }

    @Override
    public void supprimerCentre(String codeCentre) {
        Optional<Centre> centre = centreRepo.findByCodeCentre(codeCentre);
        if (centre.isEmpty()) {
            throw new EntityNotFoundException("Centre non trouvé avec le code centre : " + codeCentre);
        }


        Centre centreToDelete = centre.get();
        if (centreToDelete.getCabinets() != null) {
            for (Cabinet cabinet : centreToDelete.getCabinets()) {
                cabinet.getCentres().remove(centreToDelete);
            }
        }

        centreRepo.deleteByCodeCentre(codeCentre); // Modify repository to delete by codeCentre
    }

    @Override
    public CentreResponseDTO trouverCentreParId(Long id) {
        Optional<Centre> centreOptional = centreRepo.findById(id);
        return centreOptional.map(centreMapper::modelToDto).orElse(null);
    }

    @Override
    public List<Centre> findByCodes(List<String> codes) {
        return centreRepo.findByCodeCentreIn(codes);
    }

    @Override
    public List<Centre> findByIds(List<Long> ids) {
        return centreRepo.findAllById(ids);
    }

    @Override
    public Centre save(Centre centre) {
        return centreRepo.save(centre);
    }

    @Override
    public byte[] generateCentreExcel() throws IOException {
        List<Centre> centres = centreRepo.findAll();

        // Création d'un nouveau workbook et d'une feuille
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Centres");

        // Création de la ligne d'en-tête
        Row headerRow = sheet.createRow(0);
        String[] headers = {
                "Code Centre",
                "Nom Centre",
                "Adresse",
                "Type Centre",
                "Téléphone"
        };

        // Remplissage de la ligne d'en-tête
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }

        int rowNum = 1;
        // Remplissage des données
        for (Centre centre : centres) {
            // Récupération des détails du centre
            String codeCentre = centre.getCodeCentre();
            String nomCentre = centre.getNomCentre();
            String adresse = centre.getAdresse();
            String typeCentre = centre.getTypeCentre();
            String telephone = centre.getTelephone();

            // Remplissage des données du centre
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(codeCentre);
            row.createCell(1).setCellValue(nomCentre);
            row.createCell(2).setCellValue(adresse);
            row.createCell(3).setCellValue(typeCentre);
            row.createCell(4).setCellValue(telephone);
        }

        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            workbook.write(byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        }
    }
}
