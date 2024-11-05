package com.amasoft.amaclinic.repository;

import com.amasoft.amaclinic.Entity.Certification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface CertificationRepository extends JpaRepository<Certification, Long>, JpaSpecificationExecutor<Certification> {

    Optional<Certification> findByCode(String code);

    void deleteByCode(String code);

    boolean existsByCode(String code);


    void deleteByConsultationCodeConsultation(String codeConsultation);

}
