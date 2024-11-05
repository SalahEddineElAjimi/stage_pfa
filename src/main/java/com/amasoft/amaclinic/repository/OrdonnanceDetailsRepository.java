package com.amasoft.amaclinic.repository;

import com.amasoft.amaclinic.Entity.OrdonnanceDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrdonnanceDetailsRepository extends JpaRepository<OrdonnanceDetails, Long>,JpaSpecificationExecutor<OrdonnanceDetails>{
    
        Optional<OrdonnanceDetails> findByCodeOrdonnanceDetails (String codeOrdonnanceDetails);


        void deleteByCodeOrdonnanceDetails (String codeOrdonnanceDetails);

    
}
