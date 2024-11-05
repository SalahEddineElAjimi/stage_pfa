package com.amasoft.amaclinic.repository;

import com.amasoft.amaclinic.Entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient,Long>, JpaSpecificationExecutor<Patient> {

    Optional<Patient> findPatientByCodePatient(String codePatient);
    void deleteByCodePatient(String codePatient);




}

