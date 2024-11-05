package com.amasoft.amaclinic.Service;

import com.amasoft.amaclinic.criteria.OrdonnanceDetailsCriteria;
import com.amasoft.amaclinic.dto.request.OrdonnanceDetailsRequestDTO;
import com.amasoft.amaclinic.dto.response.OrdonnanceDetailsResponseDTO;
import org.springframework.data.domain.Page;

public interface OrdonnanceDetailsService {

    OrdonnanceDetailsResponseDTO addOrdonnanceDetails(OrdonnanceDetailsRequestDTO ordonnanceDetailsRequestDto);
    OrdonnanceDetailsResponseDTO updateOrdonnanceDetails(OrdonnanceDetailsRequestDTO ordonnanceDetailsRequestDTO);
    void  deleteOrdonnanceDetails(String codeOrdonnanceDetails);

    Page<OrdonnanceDetailsResponseDTO> findOrdonnanceDetailsByCriteria(OrdonnanceDetailsCriteria ordonnanceDetailsCriteria, int page , int size);


}
