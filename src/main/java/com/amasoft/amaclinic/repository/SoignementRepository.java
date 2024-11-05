package com.amasoft.amaclinic.repository;

import com.amasoft.amaclinic.Entity.Soignement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SoignementRepository extends JpaRepository<Soignement,Long> , JpaSpecificationExecutor<Soignement> {
    Optional<Soignement> findSoignementByCodeSoignement(String codeSoignement);
    void deleteByCodeSoignement(String codeSoignement);


}
