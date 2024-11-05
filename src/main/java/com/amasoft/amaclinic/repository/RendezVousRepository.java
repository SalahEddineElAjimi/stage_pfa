package com.amasoft.amaclinic.repository;


import com.amasoft.amaclinic.Entity.Medecin;
import com.amasoft.amaclinic.Entity.RendezVous;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface RendezVousRepository extends JpaRepository<RendezVous,Long>, JpaSpecificationExecutor<RendezVous> {
    Optional<RendezVous> findByCodeRendezVous(String codeRendezVous);
    void deleteByCodeRendezVous(String codeRendezVous);

    @Query("SELECT r FROM RendezVous r WHERE r.medecin.codeMedecin = :codeMedecin AND " +
            "(r.dateDebutRDV < :dateFinRDV AND r.dateFinRDV > :dateDebutRDV)")
    List<RendezVous> findOverlappingRendezVous(@Param("codeMedecin") String codeMedecin,
                                               @Param("dateDebutRDV") LocalDateTime dateDebutRDV,
                                               @Param("dateFinRDV") LocalDateTime dateFinRDV);

    @Query("SELECT r FROM RendezVous r WHERE r.medecin.codeMedecin = :codeMedecin AND " +
            "(r.dateDebutRDV < :dateFinRDV AND r.dateFinRDV > :dateDebutRDV) AND " +
            "r.codeRendezVous != :codeRendezVous")
    List<RendezVous> findOverlappingRendezVousExcludingCurrent(@Param("codeMedecin") String codeMedecin,
                                                               @Param("dateDebutRDV") LocalDateTime dateDebutRDV,
                                                               @Param("dateFinRDV") LocalDateTime dateFinRDV,
                                                               @Param("codeRendezVous") String codeRendezVous);
}

