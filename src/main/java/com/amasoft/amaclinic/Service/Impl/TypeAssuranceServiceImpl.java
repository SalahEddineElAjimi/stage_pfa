package com.amasoft.amaclinic.Service.Impl;


import com.amasoft.amaclinic.Entity.TypeAssurance;
import com.amasoft.amaclinic.Service.TypeAssuranceService;
import com.amasoft.amaclinic.dto.request.TypeAssuranceRequestDto;
import com.amasoft.amaclinic.dto.response.TypeAssuranceResponseDto;
import com.amasoft.amaclinic.exception.EntityAlreadyExisteException;
import com.amasoft.amaclinic.exception.EntityNotFoundException;
import com.amasoft.amaclinic.criteria.TypeAssuranceCriteria;

import com.amasoft.amaclinic.mapper.TypeAssuranceMapper;
import com.amasoft.amaclinic.repository.TypeAssuranceRepository;
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
public class TypeAssuranceServiceImpl implements TypeAssuranceService {
    private TypeAssuranceRepository typeAssuranceRepository;
    private TypeAssuranceMapper typeAssuranceMapper;

    @Override
    public TypeAssuranceResponseDto addTypeAssurance(TypeAssuranceRequestDto typeAssuranceRequestDto) {
        String generatedCodeTypeAssurance = "TPAS" + UUID.randomUUID().toString().replace("-", "").substring(0, 6);
        TypeAssurance typeAssuranceToSave = typeAssuranceMapper.dtoToModel(typeAssuranceRequestDto);
        typeAssuranceToSave.setCodeAssuranceType(generatedCodeTypeAssurance);

        Optional<TypeAssurance> existingTypeAssurance = typeAssuranceRepository.findTypeAssuranceByCodeAssuranceType(typeAssuranceToSave.getCodeAssuranceType());
        if (existingTypeAssurance.isPresent()) {
            throw new EntityAlreadyExisteException("Ce type d'assurance existe déjà avec l'identifiant : " + typeAssuranceRequestDto.getCodeAssuranceType());
        }
        TypeAssurance savedTypeAssurance = typeAssuranceRepository.save(typeAssuranceToSave);
        return typeAssuranceMapper.modelToDto(savedTypeAssurance);
    }

    @Override
    public TypeAssuranceResponseDto updateTypeAssurance(TypeAssuranceRequestDto typeAssuranceRequestDto) {
        Optional<TypeAssurance> existingTypeAss = typeAssuranceRepository.findTypeAssuranceByCodeAssuranceType(typeAssuranceRequestDto.getCodeAssuranceType());
        if (existingTypeAss.isEmpty()){
            throw new EntityNotFoundException("Type d'assurance non trouvé ");
        }
        TypeAssurance typeAssuranceToUpdate = typeAssuranceMapper.dtoToModel(typeAssuranceRequestDto);
        typeAssuranceToUpdate.setId(existingTypeAss.get().getId());
        typeAssuranceToUpdate.setCodeAssuranceType(existingTypeAss.get().getCodeAssuranceType());
        TypeAssurance updatedTypeAssurance= typeAssuranceRepository.save(typeAssuranceToUpdate);
        return typeAssuranceMapper.modelToDto(updatedTypeAssurance);
    }

    @Override
    public void deleteTypeAssurance(String codeTypeAssurance) {
        Optional<TypeAssurance> typeAssurance = typeAssuranceRepository.findTypeAssuranceByCodeAssuranceType(codeTypeAssurance);
        if (typeAssurance.isEmpty()){
            throw new EntityNotFoundException("Type d'assurance non trouvé avec le code :  "+codeTypeAssurance);
        }

        typeAssuranceRepository.deleteByCodeAssuranceType(codeTypeAssurance);
    }


    @Override
    public Page<TypeAssuranceResponseDto> findTypeAssuranceByCriteria(TypeAssuranceCriteria typeAssuranceCriteria, int page, int size) {
        PageRequest pageable = PageRequest.of(page, size);
        Page<TypeAssurance> TypeAssurancePage = typeAssuranceRepository.findAll((root, query, criteriaBuilder) -> {
            List<Predicate> predicateList = new ArrayList<>();
            if (typeAssuranceCriteria.getId() != null){
                predicateList.add(criteriaBuilder.equal(root.get("id"),typeAssuranceCriteria.getId()));
            }
            if (typeAssuranceCriteria.getNomAssurance() != null){
                predicateList.add(criteriaBuilder.equal(root.get("nomAssurance"),typeAssuranceCriteria.getNomAssurance()));
            }
            if (typeAssuranceCriteria.getTypeAssurance() != null){
                predicateList.add(criteriaBuilder.equal(root.get("typeAssurance"),typeAssuranceCriteria.getTypeAssurance()));
            }

            if (typeAssuranceCriteria.getCodeAssuranceType() != null){
                predicateList.add(criteriaBuilder.equal(root.get("typeAssuranceCode"),typeAssuranceCriteria.getCodeAssuranceType()));
            }

            return criteriaBuilder.and(predicateList.toArray(new Predicate[0]));

        } , pageable);
        return typeAssuranceMapper.modelToDtos(TypeAssurancePage);
    }

    @Override
    public byte[] generateTypeAssurance() throws IOException {
        List<TypeAssurance> typeAssurances = typeAssuranceRepository.findAll();
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("TypeAssurances");
        Row headerRow = sheet.createRow(0);
        String[] headers = {
                "ID",
                "Code Assurance Type",
                "Nom Assurance",
                "Type Assurance",
                "Description"
        };

        // Création de l'en-tête
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }

        int rowNum = 1;
        // Remplissage des données
        for (TypeAssurance typeAssurance : typeAssurances) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(typeAssurance.getId());
            row.createCell(1).setCellValue(typeAssurance.getCodeAssuranceType());
            row.createCell(2).setCellValue(typeAssurance.getNomAssurance());
            row.createCell(3).setCellValue(typeAssurance.getTypeAssurance());
            row.createCell(4).setCellValue(typeAssurance.getDescription());
        }

        // Écriture du fichier Excel en mémoire et retour en tableau d'octets
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            workbook.write(byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        }
    }
}
