package com.amasoft.amaclinic.Service.Impl;

import com.amasoft.amaclinic.Entity.Medicament;
import com.amasoft.amaclinic.Entity.OrdonnanceDetails;
import com.amasoft.amaclinic.Service.MedicamentService;
import com.amasoft.amaclinic.criteria.MedicamentCriteria;
import com.amasoft.amaclinic.dto.request.MedicamentRequestDTO;
import com.amasoft.amaclinic.dto.response.MedicamentResponseDTO;
import com.amasoft.amaclinic.exception.EntityAlreadyExisteException;
import com.amasoft.amaclinic.exception.EntityNotFoundException;
import com.amasoft.amaclinic.mapper.MedicamentMapper;
import com.amasoft.amaclinic.repository.MedicamentRepository;
import com.amasoft.amaclinic.repository.OrdonnanceDetailsRepository;
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
public class MedicamentServiceImpl implements MedicamentService {
    private MedicamentRepository medicamentRepository;
    private OrdonnanceDetailsRepository ordonnanceDetailsRepo;
    private MedicamentMapper medicamentMapper;

    @Override
    public MedicamentResponseDTO addMedicament(MedicamentRequestDTO medicamentRequestDto) {
        String generatedCodeMedicament = "MDC" + UUID.randomUUID().toString().replace("-", "").substring(0, 8);
        Medicament medicamentToSave = medicamentMapper.dtoToModel(medicamentRequestDto);
        medicamentToSave.setCodeMedicament(generatedCodeMedicament);


        Optional<Medicament> existingmedicament = medicamentRepository.findByCodeMedicament(medicamentToSave.getCodeMedicament());
        if (existingmedicament.isPresent()) {
            throw new EntityAlreadyExisteException("Medicament existe déjà avec l'identifiant : " + medicamentRequestDto.getCodeMedicament());
        }
        Medicament savedMedicament = medicamentRepository.save(medicamentToSave);
        return medicamentMapper.modelToDto(savedMedicament);
    }

    @Override
    public MedicamentResponseDTO updateMedicament(MedicamentRequestDTO medicamentRequestDto) {
        Optional<Medicament> existingmedicament = medicamentRepository.findByCodeMedicament(medicamentRequestDto.getCodeMedicament());
        if (existingmedicament.isEmpty()){
            throw new EntityNotFoundException("Medicament non trouvé ");
        }
        Medicament medicamentToUpdate = medicamentMapper.dtoToModel(medicamentRequestDto);
        medicamentToUpdate.setId(existingmedicament.get().getId());
        medicamentToUpdate.setCodeMedicament(existingmedicament.get().getCodeMedicament());


        Medicament updatedMedicament= medicamentRepository.save(medicamentToUpdate);
        return medicamentMapper.modelToDto(updatedMedicament);
    }

    @Override
    public void deleteMedicament(String codeMedicament) {
        Optional<Medicament> medicament = medicamentRepository.findByCodeMedicament(codeMedicament);
        if (medicament.isEmpty()){
            throw new EntityNotFoundException("Medicament non trouvé avec le code :  "+codeMedicament);
        }

        medicamentRepository.deleteByCodeMedicament(codeMedicament);
    }


    @Override
    public Page<MedicamentResponseDTO> findMedicamentByCriteria(MedicamentCriteria medicamentCriteria, int page, int size) {
        PageRequest pageable = PageRequest.of(page, size);
        Page<Medicament> medicamentPage = medicamentRepository.findAll((root, query, criteriaBuilder) -> {
            List<Predicate> predicateList = new ArrayList<>();
            if (medicamentCriteria.getId() != null){
                predicateList.add(criteriaBuilder.equal(root.get("id"),medicamentCriteria.getId()));
            }
            if (medicamentCriteria.getNomMedicament() != null){
                predicateList.add(criteriaBuilder.equal(root.get("nomMedicament"),medicamentCriteria.getNomMedicament()));
            }
            if (medicamentCriteria.getTypeMedicament() != null){
                predicateList.add(criteriaBuilder.equal(root.get("typeMedicament"),medicamentCriteria.getTypeMedicament()));
            }

            if (medicamentCriteria.getCodeMedicament() != null){
                predicateList.add(criteriaBuilder.equal(root.get("medicamentCode"),medicamentCriteria.getCodeMedicament()));
            }

            return criteriaBuilder.and(predicateList.toArray(new Predicate[0]));

        } , pageable);
        return medicamentMapper.modelToDtos(medicamentPage);
    }

    @Override
    public byte[] generateMedecamentExcel() throws IOException {
        List<Medicament> medicaments = medicamentRepository.findAll();
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Médicaments");
        Row headerRow = sheet.createRow(0);
        String[] headers = {"ID", "Code Médicament", "Nom Médicament", "Type Médicament"};

        // Création de l'en-tête
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }

        int rowNum = 1;
        // Remplissage des données
        for (Medicament medicament : medicaments) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(medicament.getId());
            row.createCell(1).setCellValue(medicament.getCodeMedicament());
            row.createCell(2).setCellValue(medicament.getNomMedicament());
            row.createCell(3).setCellValue(medicament.getTypeMedicament());
        }

        // Écriture du fichier Excel en mémoire et retour en tableau d'octets
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            workbook.write(byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        }
    }
}
