package com.amasoft.amaclinic.repository;

import com.amasoft.amaclinic.Entity.ExamPatient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExamPatientRepository extends JpaRepository<ExamPatient,Long>, JpaSpecificationExecutor<ExamPatient> {
    Optional<ExamPatient> findExamBycodeExamPatient(String codeExamPatient);
    void deleteBycodeExamPatient(String codeExamPatient);
}
