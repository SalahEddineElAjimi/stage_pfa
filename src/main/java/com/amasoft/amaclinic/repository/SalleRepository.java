package com.amasoft.amaclinic.repository;

import com.amasoft.amaclinic.Entity.Cabinet;
import com.amasoft.amaclinic.Entity.Salle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SalleRepository extends JpaRepository<Salle, Long>, JpaSpecificationExecutor<Salle> {

    Optional<Salle> findByCodeSalle(String codeSalle);


    List<Salle> findByCabinet(Cabinet cabinet);

    List<Salle> findByCodeSalleIn(List<String> codes);

    boolean existsByCodeSalle(String codeSalle);
    void deleteByCodeSalle(String codeSalle);


}
