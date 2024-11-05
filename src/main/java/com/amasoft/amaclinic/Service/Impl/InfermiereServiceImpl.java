package com.amasoft.amaclinic.Service.Impl;

import com.amasoft.amaclinic.Entity.Infermiere;
import com.amasoft.amaclinic.Entity.SpecialiteInfermiere;
import com.amasoft.amaclinic.Service.InfermiereService;
import com.amasoft.amaclinic.criteria.InfermiereCriteria;
import com.amasoft.amaclinic.dto.request.InfermiereRequestDTO;
import com.amasoft.amaclinic.dto.response.InfermiereResponseDTO;
import com.amasoft.amaclinic.exception.EntityAlreadyExisteException;
import com.amasoft.amaclinic.exception.EntityNotFoundException;
import com.amasoft.amaclinic.mapper.InfermiereMapper;
import com.amasoft.amaclinic.repository.InfermiereRepository;
import com.amasoft.amaclinic.repository.SpecialiteInfermiereRepository;
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
public class InfermiereServiceImpl implements InfermiereService {

    private final InfermiereRepository infermiereRepo;
    private final SpecialiteInfermiereRepository specialiteInfermRepo;
    private final InfermiereMapper infermiereMapper;

    @Override
    public Page<InfermiereResponseDTO> trouverInfirmieresParCritere(InfermiereCriteria infermiereCriteria, int page, int size) {
        PageRequest pageable = PageRequest.of(page, size);
        Page<Infermiere> infermierePage = infermiereRepo.findAll((root, query, criteriaBuilder) -> {
            List<Predicate> predicateList = new ArrayList<>();
            if (infermiereCriteria.getId() != null) {
                predicateList.add(criteriaBuilder.equal(root.get("id"), infermiereCriteria.getId()));
            }
            if (infermiereCriteria.getNom() != null) {
                predicateList.add(criteriaBuilder.equal(root.get("nom"), infermiereCriteria.getNom()));
            }
            if (infermiereCriteria.getPrenom() != null) {
                predicateList.add(criteriaBuilder.equal(root.get("prenom"), infermiereCriteria.getPrenom()));
            }
            if (infermiereCriteria.getEmail() != null) {
                predicateList.add(criteriaBuilder.equal(root.get("email"), infermiereCriteria.getEmail()));
            }
            if (infermiereCriteria.getVille() != null) {
                predicateList.add(criteriaBuilder.equal(root.get("ville"), infermiereCriteria.getVille()));
            }
            if (infermiereCriteria.getCin() != null) {
                predicateList.add(criteriaBuilder.equal(root.get("cin"), infermiereCriteria.getCin()));
            }
            if (infermiereCriteria.getAdresse() != null) {
                predicateList.add(criteriaBuilder.equal(root.get("adresse"), infermiereCriteria.getAdresse()));
            }
            if (infermiereCriteria.getTelephone() != null) {
                predicateList.add(criteriaBuilder.equal(root.get("telephone"), infermiereCriteria.getTelephone()));
            }
            return criteriaBuilder.and(predicateList.toArray(new Predicate[0]));
        }, pageable);
        return infermiereMapper.modelToDtos(infermierePage);
    }

    @Override
    public InfermiereResponseDTO ajouterInfermiere(InfermiereRequestDTO infermiereRequestDto) {
        String generatedCodeInferm = "INF" + UUID.randomUUID().toString().replace("-", "").substring(0, 8);


        Infermiere infermiereToSave = infermiereMapper.dtoToModel(infermiereRequestDto);
        infermiereToSave.setCodeInferm(generatedCodeInferm);


        Optional<Infermiere> existingInfermiere = infermiereRepo.findInfermiereByCodeInferm(generatedCodeInferm);
        if (existingInfermiere.isPresent()) {
            throw new EntityAlreadyExisteException("Infirmière existe déjà avec le code : " + generatedCodeInferm);
        }


        Infermiere savedInfermiere = infermiereRepo.save(infermiereToSave);
        return infermiereMapper.modelToDto(savedInfermiere);
    }

    @Override
    public InfermiereResponseDTO modifierInfermiere(InfermiereRequestDTO infermiereRequestDto) {
        // Trouver l'infirmière existante
        Optional<Infermiere> existingInfermiere = infermiereRepo.findInfermiereByCodeInferm(infermiereRequestDto.getCodeInferm());
        if (existingInfermiere.isEmpty()) {
            throw new EntityNotFoundException("Infirmière non trouvée avec le Code Infirmier : " + infermiereRequestDto.getCodeInferm());
        }

        // Trouver la spécialité correspondant au code
        Optional<SpecialiteInfermiere> specialiteInferm = specialiteInfermRepo.findByCodeSpecialite(infermiereRequestDto.getCodeSpecialite());
        if (specialiteInferm.isEmpty()) {
            throw new EntityNotFoundException("Spécialité non trouvée avec le code : " + infermiereRequestDto.getCodeSpecialite());
        }

        // Mapper DTO to Entity
        Infermiere infermiereToUpdate = infermiereMapper.dtoToModel(infermiereRequestDto);
        infermiereToUpdate.setIdInfermiere(existingInfermiere.get().getIdInfermiere());
        infermiereToUpdate.setSpecialite(specialiteInferm.get()); // Utiliser le bon setter

        // Mettre à jour l'infirmière
        Infermiere updatedInfermiere = infermiereRepo.save(infermiereToUpdate);
        return infermiereMapper.modelToDto(updatedInfermiere);
    }

    @Override
    public void supprimerInfermiere(String codeInferm) {
        Optional<Infermiere> infermiere = infermiereRepo.findInfermiereByCodeInferm(codeInferm);
        if (infermiere.isEmpty()) {
            throw new EntityNotFoundException("Infirmière non trouvée avec le code : " + codeInferm);
        }
        infermiereRepo.deleteByCodeInferm(codeInferm);
    }

    @Override
    public Optional<Infermiere> findByCodeInferm(String codeInferm) {
        return infermiereRepo.findByCodeInferm(codeInferm);
    }

    @Override
    public byte[] generateInfermiereExcel() throws IOException {
        List<Infermiere> infermieres = infermiereRepo.findAll();
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("infermieres");
        Row headerRow = sheet.createRow(0);
        String[] headers = {"codeInfermiere" , "nom" , "prenom","email","ville","cin","adresse","telephone","codeSpecialite"};
        for(int i=0 ; i < headers.length; i++)
        {
            Cell cell =headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }
        int rowNum = 1 ;
        for(Infermiere infermiere : infermieres){
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(infermiere.getCodeInferm());
            row.createCell(1).setCellValue(infermiere.getNom());
            row.createCell(2).setCellValue(infermiere.getPrenom());
            row.createCell(3).setCellValue(infermiere.getEmail());
            row.createCell(4).setCellValue(infermiere.getVille());
            row.createCell(5).setCellValue(infermiere.getCin());
            row.createCell(6).setCellValue(infermiere.getAdresse());
            row.createCell(7).setCellValue(infermiere.getTelephone());
        }
        try(ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            workbook.write(byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();

        }
    }
}
