package com.amasoft.amaclinic.repository;

import com.amasoft.amaclinic.Entity.Paiment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface PaimentRepository extends JpaRepository<Paiment,Long>, JpaSpecificationExecutor<Paiment> {

    Optional<Paiment> findByCodePaiment(String codePaiment);

    void deleteByCodePaiment(String codePaiment);

    boolean existsByCodePaiment(String codePaiment);

    void deleteByConsultationId(Long consultationId);

}
