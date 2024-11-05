package com.amasoft.amaclinic.Service.Impl;

import com.amasoft.amaclinic.Entity.MedecinSpecialites;
import com.amasoft.amaclinic.Service.SpecialiteMedService;
import com.amasoft.amaclinic.criteria.SpecialiteCriteria;
import com.amasoft.amaclinic.dto.request.SpecialiteMedRequestDto;
import com.amasoft.amaclinic.dto.response.SpecialiteMedResponseDto;
import com.amasoft.amaclinic.exception.EntityAlreadyExisteException;
import com.amasoft.amaclinic.exception.EntityNotFoundException;
import com.amasoft.amaclinic.mapper.SpecialiteMedMapper;
import com.amasoft.amaclinic.repository.SpecialiteMedRepository;
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
public class SpecialiteMedServiceIMP implements SpecialiteMedService {
    private SpecialiteMedRepository specialiteMedRepository;
    private SpecialiteMedMapper specialiteMedMapper;
    @Override
    public SpecialiteMedResponseDto addSpecialite(SpecialiteMedRequestDto specialiteMedRequestDto)
    {
        String generateCodeSpecialite = "SPMED"+ UUID.randomUUID().toString().replace("-" , "").substring(0,8);
        MedecinSpecialites specialiteToSave = specialiteMedMapper.dtoToModel(specialiteMedRequestDto);
        specialiteToSave.setCodeSpecialite(generateCodeSpecialite);
        Optional<MedecinSpecialites> exisitingSpecialite = specialiteMedRepository.findSpecialiteBycodeSpecialite(specialiteToSave.getCodeSpecialite());
        if (exisitingSpecialite.isPresent())
        {
            throw new EntityAlreadyExisteException("speciaite existe deja avec le code: "+specialiteMedRequestDto.getCodeSpecialite());
        }
        MedecinSpecialites savedspecialite = specialiteMedRepository.save(specialiteToSave);
        return specialiteMedMapper.modelToDto(savedspecialite);
    }

    @Override
    public SpecialiteMedResponseDto updateSpecialite(SpecialiteMedRequestDto specialiteMedRequestDto) {
        Optional<MedecinSpecialites> existingSPE = specialiteMedRepository.findSpecialiteBycodeSpecialite(specialiteMedRequestDto.getCodeSpecialite());
        if (existingSPE.isEmpty())
        {
            throw new EntityNotFoundException("specialite not found ");
        }
        MedecinSpecialites specialiteToUpdate = specialiteMedMapper.dtoToModel(specialiteMedRequestDto);
        specialiteToUpdate.setId(existingSPE.get().getId());
        specialiteToUpdate.setCodeSpecialite(existingSPE.get().getCodeSpecialite());
        MedecinSpecialites updatedSpecialite = specialiteMedRepository.save(specialiteToUpdate);
        return specialiteMedMapper.modelToDto(updatedSpecialite);
    }

    @Override
    public void deleteSpecialite(String codeSpecialite) {
        Optional<MedecinSpecialites> specialite = specialiteMedRepository.findSpecialiteBycodeSpecialite(codeSpecialite);
        if (specialite.isEmpty())
        {
            throw new EntityNotFoundException("Specialite n'as pas ete trouvee code :" +codeSpecialite);
        }
        specialiteMedRepository.deleteBycodeSpecialite(codeSpecialite);
    }

    @Override
    public Page<SpecialiteMedResponseDto> findSpecialiteByCriteria(SpecialiteCriteria SpecialiteCriteria, int page, int size) {
        PageRequest pageable = PageRequest.of(page, size);
        Page<MedecinSpecialites> specialitepage = specialiteMedRepository.findAll((root, query, criteriaBuilder) -> {
            List<Predicate> predicateList = new ArrayList<>();
            if (SpecialiteCriteria.getId() != null){
                predicateList.add(criteriaBuilder.equal(root.get("id"),SpecialiteCriteria.getId()));
            }
            if (SpecialiteCriteria.getCodeSpecialite() != null){
                predicateList.add(criteriaBuilder.equal(root.get("codeSpecialite"),SpecialiteCriteria.getCodeSpecialite()));
            }
            if (SpecialiteCriteria.getNom() !=null){
                predicateList.add(criteriaBuilder.equal(root.get("nom"),SpecialiteCriteria.getNom()));
            }
            return criteriaBuilder.and(predicateList.toArray(new Predicate[0]));
        }, pageable);
        return specialiteMedMapper.modelToDtos(specialitepage);
    }

    @Override
    public byte[] generateExcel() throws IOException {
        List<MedecinSpecialites> specialites = specialiteMedRepository.findAll();
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("SpecialitesMed");
        Row headerRow = sheet.createRow(0);
        String[] headers = {"Code Spécialité", "Nom Spécialité"};

        // Création de l'en-tête
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }

        int rowNum = 1;
        // Remplissage des données
        for (MedecinSpecialites specialite : specialites) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(specialite.getCodeSpecialite());
            row.createCell(1).setCellValue(specialite.getNomSpecialite());
        }

        // Écriture du fichier Excel en mémoire et retour en tableau d'octets
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            workbook.write(byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        }
    }
}
