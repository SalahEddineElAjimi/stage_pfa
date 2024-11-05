package com.amasoft.amaclinic.repository;

import com.amasoft.amaclinic.Entity.Medecin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MedecinRepository extends JpaRepository<Medecin,Long>, JpaSpecificationExecutor<Medecin> {

    Optional<Medecin> findMedecinByCodeMedecin(String codeMedecin);
    void deleteByCodeMedecin(String codeMedecin);




}

