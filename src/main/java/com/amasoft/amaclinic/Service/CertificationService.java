package com.amasoft.amaclinic.Service;

import com.amasoft.amaclinic.criteria.CertificationCriteria;
import com.amasoft.amaclinic.dto.request.CertificationRequestDto;
import com.amasoft.amaclinic.dto.response.CertificationResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Service
public interface CertificationService {

    /**
     * Crée une nouvelle certification.
     *
     * @param certificationRequestDto les détails de la certification à créer
     * @return la certification créée
     */
    CertificationRequestDto creerCertification(CertificationRequestDto certificationRequestDto);

    /**
     * Met à jour une certification existante.
     *
     * @param codeCertification       le code de la certification à mettre à jour
     * @param certificationRequestDto les nouveaux détails de la certification
     * @return la certification mise à jour
     */
    CertificationRequestDto mettreAJourCertification(String codeCertification, CertificationRequestDto certificationRequestDto);

    /**
     * Supprime une certification par son code.
     *
     * @param codeCertification le code de la certification à supprimer
     */
    void supprimerCertificationParCode(String codeCertification);

    /**
     * Obtient une certification par son code.
     *
     * @param codeCertification le code de la certification à récupérer
     * @return une Optional contenant la certification si trouvée
     */
    Optional<CertificationRequestDto> obtenirCertificationParCode(String codeCertification);

    /**
     * Trouve des certifications en fonction des critères de recherche.
     *
     * @param certificationCriteria les critères de recherche des certifications
     * @param page le numéro de page pour la pagination
     * @param size la taille de la page pour la pagination
     * @return une page de certifications correspondant aux critères
     */
    Page<CertificationResponseDTO> trouverCertificationParCritères(CertificationCriteria certificationCriteria, int page, int size);
    byte[] generateCertificationExcel() throws IOException;

}
