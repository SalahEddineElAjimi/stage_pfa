package com.amasoft.amaclinic.repository;

import com.amasoft.amaclinic.Entity.Centre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface CentreRepository extends JpaRepository<Centre, Long>, JpaSpecificationExecutor<Centre> {
    Optional<Centre> findByCodeCentre(String codeCentre);

    List<Centre> findByCodeCentreIn(List<String> codes);
    Optional<Centre> findByNomCentre(String nom);

    List<Centre> findAllById(Iterable<Long> ids);

    void deleteByCodeCentre(String codeCentre);

}
