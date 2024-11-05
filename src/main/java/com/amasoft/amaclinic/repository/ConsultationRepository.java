package com.amasoft.amaclinic.repository;


import com.amasoft.amaclinic.Entity.AssurancePatient;
import com.amasoft.amaclinic.Entity.Consultation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository

public interface ConsultationRepository extends JpaRepository<Consultation,Long>, JpaSpecificationExecutor<Consultation> {

    Optional<Consultation> findConsultationByCodeConsultation(String codeConsultation);
    void deleteConsultationByCodeConsultation(String codeConsultation);
}
