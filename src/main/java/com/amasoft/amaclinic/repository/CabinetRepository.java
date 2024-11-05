package com.amasoft.amaclinic.repository;

import com.amasoft.amaclinic.Entity.Cabinet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CabinetRepository extends JpaRepository<Cabinet,Long> , JpaSpecificationExecutor<Cabinet> {

    Optional<Cabinet> findByCodeCabinet(String codeCabinet);

    void deleteByCodeCabinet(String codeCabinet);


}
