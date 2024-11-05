package com.amasoft.amaclinic.Service.Impl;

import com.amasoft.amaclinic.Entity.Medecin;
import com.amasoft.amaclinic.Entity.MedecinSpecialites;
import com.amasoft.amaclinic.Service.MedecinService;
import com.amasoft.amaclinic.criteria.MedecinCriteria;
import com.amasoft.amaclinic.dto.request.MedecinRequestDto;
import com.amasoft.amaclinic.dto.response.MedecinResponseDto;
import com.amasoft.amaclinic.mapper.MedecinMapper;
import com.amasoft.amaclinic.repository.MedecinRepository;
import com.amasoft.amaclinic.exception.EntityAlreadyExisteException;
import com.amasoft.amaclinic.exception.EntityNotFoundException;
import com.amasoft.amaclinic.repository.SpecialiteInfermiereRepository;
import com.amasoft.amaclinic.repository.SpecialiteMedRepository;
import jakarta.persistence.criteria.Predicate;
import jakarta.servlet.http.HttpServlet;
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


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
@Transactional
public class MedecinServiceImpl implements MedecinService {
    private MedecinRepository medecinRepository;
    private MedecinMapper medecinMapper;
    private SpecialiteMedRepository medecinSpecialitesRepository;

    @Override
    public MedecinResponseDto addMedecin(MedecinRequestDto medecinRequestDto) {
        String generatedCodeMedecin = "MDC" + UUID.randomUUID().toString().replace("-", "").substring(0, 8);
        Medecin medecinToSave = medecinMapper.dtoToModel(medecinRequestDto);
        medecinToSave.setCodeMedecin(generatedCodeMedecin);

        // Fetch Specialite from repository
        MedecinSpecialites specialite = medecinSpecialitesRepository.findSpecialiteBycodeSpecialite(medecinRequestDto.getCodeSpecialite())
                .orElseThrow(() -> new EntityNotFoundException("Specialite non trouvée pour le code : " + medecinRequestDto.getCodeSpecialite()));
        medecinToSave.setSpecialite(specialite);

        Optional<Medecin> existingMedecin = medecinRepository.findMedecinByCodeMedecin(medecinToSave.getCodeMedecin());
        if (existingMedecin.isPresent()) {
            throw new EntityAlreadyExisteException("Médecin existe déjà avec l'identifiant : " + medecinRequestDto.getCodeMedecin());
        }
        Medecin savedMedecin = medecinRepository.save(medecinToSave);
        return medecinMapper.modelToDto(savedMedecin);
    }

    @Override
    public MedecinResponseDto UpdateMedecin(MedecinRequestDto medecinRequestDto) {
        Optional<Medecin> existingMed = medecinRepository.findMedecinByCodeMedecin(medecinRequestDto.getCodeMedecin());
        if (existingMed.isEmpty()){
            throw new EntityNotFoundException("Médecin non trouvé ");
        }
        Medecin medecinToUpdate = medecinMapper.dtoToModel(medecinRequestDto);

        medecinToUpdate.setId(existingMed.get().getId());
        medecinToUpdate.setCodeMedecin(existingMed.get().getCodeMedecin());

        // Fetch Specialite from repository
        MedecinSpecialites specialite = medecinSpecialitesRepository.findSpecialiteBycodeSpecialite(medecinRequestDto.getCodeSpecialite())
                .orElseThrow(() -> new EntityNotFoundException("Specialite non trouvée pour le code : " + medecinRequestDto.getCodeSpecialite()));
        medecinToUpdate.setSpecialite(specialite);

        Medecin updatedMedecin = medecinRepository.save(medecinToUpdate);
        return medecinMapper.modelToDto(updatedMedecin);
    }

    @Override
    public void deleteMedecin(String codeMedecin) {
        Optional<Medecin> medecin = medecinRepository.findMedecinByCodeMedecin(codeMedecin);
        if (medecin.isEmpty()){
            throw new EntityNotFoundException("Médecin non trouvé avec le code :  "+codeMedecin);
        }

        medecinRepository.deleteByCodeMedecin(codeMedecin);
    }


    @Override
    public Page<MedecinResponseDto> findMedecinByCriteria(MedecinCriteria medecinCriteria, int page, int size) {
        PageRequest pageable = PageRequest.of(page, size);
        Page<Medecin> MedecinPage = medecinRepository.findAll((root, query, criteriaBuilder) -> {
            List<Predicate> predicateList = new ArrayList<>();
            if (medecinCriteria.getId() != null){
                predicateList.add(criteriaBuilder.equal(root.get("id"),medecinCriteria.getId()));
            }
            if (medecinCriteria.getPrenom() != null){
                predicateList.add(criteriaBuilder.equal(root.get("prenom"),medecinCriteria.getPrenom()));
            }
            if (medecinCriteria.getNom() != null){
                predicateList.add(criteriaBuilder.equal(root.get("nom"),medecinCriteria.getNom()));
            }
            if (medecinCriteria.getCin() != null){
                predicateList.add(criteriaBuilder.equal(root.get("cin"),medecinCriteria.getCin()));
            }
            if (medecinCriteria.getMedecinCode() != null){
                predicateList.add(criteriaBuilder.equal(root.get("medecinCode"),medecinCriteria.getMedecinCode()));
            }

            return criteriaBuilder.and(predicateList.toArray(new Predicate[0]));

        } , pageable);
        return medecinMapper.modelToDtos(MedecinPage);
    }

    @Override
    public byte[] generateMedecinExcel() throws IOException {
        List<Medecin> medecins = medecinRepository.findAll();
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("medecins");
        Row headerRow = sheet.createRow(0);
        String[] headers = {"code" ,"prenom" , "nom" , "cin" , "telephone" , "email" , "adresse" , "ville"};
        for (int i = 0 ; i < headers.length; i++)
        {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }
        int rowNum = 1 ;
        for (Medecin medecin :medecins)
        {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(medecin.getCodeMedecin());
            row.createCell(1).setCellValue(medecin.getNom());
            row.createCell(2).setCellValue(medecin.getPrenom());
            row.createCell(3).setCellValue(medecin.getCin());
            row.createCell(4).setCellValue(medecin.getTelephone());
            row.createCell(5).setCellValue(medecin.getEmail());
            row.createCell(6).setCellValue(medecin.getAddress());
            row.createCell(7).setCellValue(medecin.getVille());
        }
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()){
            workbook.write(byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        }

    }
}

