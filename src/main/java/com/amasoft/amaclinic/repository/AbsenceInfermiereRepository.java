package com.amasoft.amaclinic.repository;

import com.amasoft.amaclinic.Entity.AbsenceInfermiere;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AbsenceInfermiereRepository extends JpaRepository<AbsenceInfermiere, Long>, JpaSpecificationExecutor<AbsenceInfermiere> {

    Optional<AbsenceInfermiere> findByCodeAbscence(String codeAbscence);

    void deleteByCodeAbscence(String codeAbscence);
}
