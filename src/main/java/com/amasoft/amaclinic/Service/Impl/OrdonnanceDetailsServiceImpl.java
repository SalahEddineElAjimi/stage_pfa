package com.amasoft.amaclinic.Service.Impl;

import com.amasoft.amaclinic.Entity.*;
import com.amasoft.amaclinic.Service.OrdonnanceDetailsService;
import com.amasoft.amaclinic.criteria.OrdonnanceDetailsCriteria;
import com.amasoft.amaclinic.dto.request.OrdonnanceDetailsRequestDTO;
import com.amasoft.amaclinic.dto.response.OrdonnanceDetailsResponseDTO;
import com.amasoft.amaclinic.exception.EntityNotFoundException;
import com.amasoft.amaclinic.mapper.OrdonnanceDetailsMapper;
import com.amasoft.amaclinic.repository.ConsultationRepository;
import com.amasoft.amaclinic.repository.MedicamentRepository;
import com.amasoft.amaclinic.repository.OrdonnanceDetailsRepository;
import com.amasoft.amaclinic.repository.OrdonnanceRepository;
import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
@Transactional
public class OrdonnanceDetailsServiceImpl implements OrdonnanceDetailsService {
    private OrdonnanceDetailsRepository ordonnanceDetailsRepository;
    private OrdonnanceRepository ordonnanceRepo;
    private MedicamentRepository medicamentRepo;

    private OrdonnanceDetailsMapper ordonnanceDetailsMapper;

    @Override
    public OrdonnanceDetailsResponseDTO addOrdonnanceDetails(OrdonnanceDetailsRequestDTO ordonnanceDetailsRequestDto) {
        String generatedCodeOrdonnanceDetails = "ORDDT" + UUID.randomUUID().toString().replace("-", "").substring(0, 7);
        OrdonnanceDetails ordonnanceDetailsToSave = ordonnanceDetailsMapper.dtoToModel(ordonnanceDetailsRequestDto);
        ordonnanceDetailsToSave.setCodeOrdonnanceDetails(generatedCodeOrdonnanceDetails);


        Optional<Ordonnance> ordonnance = ordonnanceRepo.findByCodeOrdonnance(ordonnanceDetailsRequestDto.getCodeOrdonnance());
        ordonnanceDetailsToSave.setOrdonnance(ordonnance.get());

        Optional<Medicament> medicament = medicamentRepo.findByCodeMedicament(ordonnanceDetailsRequestDto.getCodeMedicament());
        ordonnanceDetailsToSave.setMedicament(medicament.get());

        OrdonnanceDetails savedOrdonnanceDetails = ordonnanceDetailsRepository.save(ordonnanceDetailsToSave);
        return ordonnanceDetailsMapper.modelToDto(savedOrdonnanceDetails);
    }



    @Override
    public OrdonnanceDetailsResponseDTO updateOrdonnanceDetails(OrdonnanceDetailsRequestDTO ordonnanceDetailsRequestDto) {
        Optional<OrdonnanceDetails> existingord = ordonnanceDetailsRepository.findByCodeOrdonnanceDetails(ordonnanceDetailsRequestDto.getCodeOrdonnanceDetails());
        if (existingord.isEmpty()){
            throw new EntityNotFoundException("OrdonnanceDetails non trouvé ");
        }
        OrdonnanceDetails ordonnanceDetailsToUpdate = ordonnanceDetailsMapper.dtoToModel(ordonnanceDetailsRequestDto);
        ordonnanceDetailsToUpdate.setId(existingord.get().getId());
        ordonnanceDetailsToUpdate.setCodeOrdonnanceDetails(existingord.get().getCodeOrdonnanceDetails());

        ordonnanceDetailsToUpdate.setOrdonnance(ordonnanceRepo.findByCodeOrdonnance(ordonnanceDetailsRequestDto.getCodeOrdonnance()).get());
        ordonnanceDetailsToUpdate.setMedicament(medicamentRepo.findByCodeMedicament(ordonnanceDetailsRequestDto.getCodeMedicament()).get());

        OrdonnanceDetails updatedOrdonnanceDetails = ordonnanceDetailsRepository.save(ordonnanceDetailsToUpdate);
        return ordonnanceDetailsMapper.modelToDto(updatedOrdonnanceDetails);

    }



    @Override
    public void deleteOrdonnanceDetails(String codeOrdonnanceDetails) {
        Optional<OrdonnanceDetails> ordonnanceDetails = ordonnanceDetailsRepository.findByCodeOrdonnanceDetails(codeOrdonnanceDetails);
        if (ordonnanceDetails.isEmpty()){
            throw new EntityNotFoundException("OrdonnanceDetails non trouvé avec le code :  "+codeOrdonnanceDetails);
        }

        ordonnanceDetailsRepository.deleteByCodeOrdonnanceDetails(codeOrdonnanceDetails);
    }




    @Override
    public Page<OrdonnanceDetailsResponseDTO> findOrdonnanceDetailsByCriteria(OrdonnanceDetailsCriteria ordonnanceDetailsCriteria, int page, int size) {
        PageRequest pageable = PageRequest.of(page, size);
        Page<OrdonnanceDetails> ordonnanceDetailsPage = ordonnanceDetailsRepository.findAll((root, query, criteriaBuilder) -> {
            List<Predicate> predicateList = new ArrayList<>();
            if (ordonnanceDetailsCriteria.getId() != null){
                predicateList.add(criteriaBuilder.equal(root.get("id"),ordonnanceDetailsCriteria.getId()));
            }
            if (ordonnanceDetailsCriteria.getDose() != null){
                predicateList.add(criteriaBuilder.equal(root.get("dose"),ordonnanceDetailsCriteria.getDose()));
            }
            if (ordonnanceDetailsCriteria.getHoraires() != null){
                predicateList.add(criteriaBuilder.equal(root.get("horaires"),ordonnanceDetailsCriteria.getHoraires()));
            }
            if (ordonnanceDetailsCriteria.getDureeTraitement() !=null){
                predicateList.add(criteriaBuilder.equal(root.get("dureeTraitement"),ordonnanceDetailsCriteria.getDureeTraitement()));
            }
            if (ordonnanceDetailsCriteria.getFrequence() !=null){
                predicateList.add(criteriaBuilder.equal(root.get("frequence"),ordonnanceDetailsCriteria.getFrequence()));
            }
            if (ordonnanceDetailsCriteria.getInstructionsSpeciales() !=null){
                predicateList.add(criteriaBuilder.equal(root.get("instructionsSpeciales"),ordonnanceDetailsCriteria.getInstructionsSpeciales()));
            }

            if (ordonnanceDetailsCriteria.getCodeOrdonnanceDetails() != null){
                predicateList.add(criteriaBuilder.equal(root.get("ordonnanceDetailsCode"),ordonnanceDetailsCriteria.getCodeOrdonnanceDetails()));
            }

            return criteriaBuilder.and(predicateList.toArray(new Predicate[0]));

        } , pageable);
        return ordonnanceDetailsMapper.modelToDtos(ordonnanceDetailsPage);
    }
    
}
