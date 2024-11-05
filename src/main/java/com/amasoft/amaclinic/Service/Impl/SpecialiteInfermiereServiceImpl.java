package com.amasoft.amaclinic.Service.Impl;

import com.amasoft.amaclinic.Entity.Infermiere;
import com.amasoft.amaclinic.Entity.RendezVous;
import com.amasoft.amaclinic.Entity.SpecialiteInfermiere;
import com.amasoft.amaclinic.Service.SpecialiteInfermiereService;
import com.amasoft.amaclinic.criteria.SpecialiteInfeCriteria;
import com.amasoft.amaclinic.dto.request.SpecialiteInfeRequestDto;
import com.amasoft.amaclinic.dto.response.SpecialiteInfeResponseDto;
import com.amasoft.amaclinic.exception.EntityNotFoundException;
import com.amasoft.amaclinic.mapper.SpecialiteInfermiereMapper;
import com.amasoft.amaclinic.repository.SpecialiteInfermiereRepository;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class SpecialiteInfermiereServiceImpl implements SpecialiteInfermiereService {

    @Autowired
    private SpecialiteInfermiereRepository repository;

    @Autowired
    private SpecialiteInfermiereMapper mapper;

    @Override
    public Page<SpecialiteInfeResponseDto> trouverSpecialitesParCritere(SpecialiteInfeCriteria specialiteInfeCriteria, int page, int size) {
        PageRequest pageable = PageRequest.of(page, size);
        Page<SpecialiteInfermiere> specialitePage = repository.findAll((root, query, criteriaBuilder) -> {
            List<Predicate> predicateList = new ArrayList<>();

            if (specialiteInfeCriteria.getIdSpecialite() != null) {
                predicateList.add(criteriaBuilder.equal(root.get("id"), specialiteInfeCriteria.getIdSpecialite()));
            }
            if (specialiteInfeCriteria.getCodeSpecialite() != null) {
                predicateList.add(criteriaBuilder.equal(root.get("codeSpecialite"), specialiteInfeCriteria.getCodeSpecialite()));
            }
            if (specialiteInfeCriteria.getNomSpecialite() != null) {
                predicateList.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("nomSpecialite")), "%" + specialiteInfeCriteria.getNomSpecialite().toLowerCase() + "%"));
            }
            if (specialiteInfeCriteria.getInfermiereCode() != null) {
                Join<SpecialiteInfermiere, Infermiere> infermiereJoin = root.join("infermiere", JoinType.INNER);
                predicateList.add(criteriaBuilder.equal(infermiereJoin.get("codeInferm"), specialiteInfeCriteria.getInfermiereCode()));
            }

            return criteriaBuilder.and(predicateList.toArray(new Predicate[0]));
        }, pageable);

        return mapper.modelToDtos(specialitePage);
    }


    @Override
    public SpecialiteInfeResponseDto ajouterSpecialite(SpecialiteInfeRequestDto specialiteInfeRequestDto) {
        String codeSpecialite = "SPEINF" + UUID.randomUUID().toString().replace("-", "").substring(0,8);
        SpecialiteInfermiere speciateToSave = mapper.dtoToModel(specialiteInfeRequestDto);
        speciateToSave.setCodeSpecialite(codeSpecialite);

        SpecialiteInfermiere savedSpecialite = repository.save(speciateToSave);
        return mapper.modelToDto(savedSpecialite);
    }

    @Override
    public SpecialiteInfeResponseDto modifierSpecialite(SpecialiteInfeRequestDto specialiteInfeRequestDto) {
        Optional<SpecialiteInfermiere> existingSpecialite = repository.findByCodeSpecialite(specialiteInfeRequestDto.getCodeSpecialite());
        if(existingSpecialite.isEmpty())
        {
            throw new EntityNotFoundException("specialite infermiere non trouve");
        }
        SpecialiteInfermiere speciateToUpdate = mapper.dtoToModel(specialiteInfeRequestDto);
        speciateToUpdate.setId(existingSpecialite.get().getId());
        speciateToUpdate.setCodeSpecialite(existingSpecialite.get().getCodeSpecialite());
        SpecialiteInfermiere updatedSpecialite = repository.save(speciateToUpdate);
        return mapper.modelToDto(updatedSpecialite);
    }

    @Override
    @Transactional
    public void supprimerSpecialite(String codeSpecialite) {
       Optional<SpecialiteInfermiere> specialiteInferimiere = repository.findByCodeSpecialite(codeSpecialite);
       if (specialiteInferimiere.isEmpty())
       {
           throw new EntityNotFoundException("specialite infermiere non trouve: "+codeSpecialite);
       }
       repository.deleteByCodeSpecialite(codeSpecialite);
    }

    @Override
    public byte[] generateExcelSpeInfe() throws IOException {
        List<SpecialiteInfermiere> specialiteInfermiereList = repository.findAll();
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("RendezVous");
        Row headerRow = sheet.createRow(0);
        String[] headers = {
                "Code RendezVous", "Status", "Type RDV", "Date Début RDV", "Date Fin RDV",
                "Code Patient", "Code Medecin"
        };

        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }

        int rowNum = 1;
        for (SpecialiteInfermiere specialiteInfermiere :specialiteInfermiereList) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(specialiteInfermiere.getCodeSpecialite());
            row.createCell(1).setCellValue(specialiteInfermiere.getNomSpecialite());

        }

        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            workbook.write(byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        }
    }
}
