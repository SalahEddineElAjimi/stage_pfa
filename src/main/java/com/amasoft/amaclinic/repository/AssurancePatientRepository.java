package com.amasoft.amaclinic.repository;

import com.amasoft.amaclinic.Entity.AssurancePatient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AssurancePatientRepository extends JpaRepository<AssurancePatient,Long>, JpaSpecificationExecutor<AssurancePatient> {

    Optional<AssurancePatient> findAssurancePatientByCodeAssurance(String codeAssurancePatient);

    @Modifying
    @Query("DELETE FROM AssurancePatient ap WHERE ap.codeAssurance = :codeAssurance")
    void deleteByCodeAssurance(@Param("codeAssurance") String codeAssurancePatient);

}
