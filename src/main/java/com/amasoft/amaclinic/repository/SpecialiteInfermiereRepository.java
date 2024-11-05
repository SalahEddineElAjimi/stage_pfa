package com.amasoft.amaclinic.repository;

import com.amasoft.amaclinic.Entity.SpecialiteInfermiere;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SpecialiteInfermiereRepository extends JpaRepository<SpecialiteInfermiere,Long>, JpaSpecificationExecutor<SpecialiteInfermiere> {

    Optional<SpecialiteInfermiere> findByCodeSpecialite(String codeSpecialite);

    void deleteByCodeSpecialite(String codeSpecialite);



}
