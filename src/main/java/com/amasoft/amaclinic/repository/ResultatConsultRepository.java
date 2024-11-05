package com.amasoft.amaclinic.repository;

import com.amasoft.amaclinic.Entity.ResultatConsultation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ResultatConsultRepository extends JpaRepository<ResultatConsultation,Long>, JpaSpecificationExecutor<ResultatConsultation> {
    Optional<ResultatConsultation> findResultatConsultByCodeResultatConsultation(String codeResultatConsultation);
    void deleteByCodeResultatConsultation(String codeResultatConsultation);


}
