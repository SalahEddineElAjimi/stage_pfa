package com.amasoft.amaclinic.repository;

import com.amasoft.amaclinic.Entity.TypeAssurance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TypeAssuranceRepository extends JpaRepository<TypeAssurance,Long>, JpaSpecificationExecutor<TypeAssurance> {

    Optional<TypeAssurance> findTypeAssuranceByCodeAssuranceType(String codeTypeAssurance);
    void deleteByCodeAssuranceType(String codeTypeAssurance);




}

