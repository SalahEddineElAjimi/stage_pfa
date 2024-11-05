package com.amasoft.amaclinic.repository;

import com.amasoft.amaclinic.Entity.MedecinSpecialites;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SpecialiteMedRepository extends JpaRepository<MedecinSpecialites, Long>,JpaSpecificationExecutor<MedecinSpecialites> {
    Optional<MedecinSpecialites> findSpecialiteBycodeSpecialite(String codeSpecialite);
    void deleteBycodeSpecialite(String codeSpecialite);

}
