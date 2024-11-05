package com.amasoft.amaclinic.repository;

import com.amasoft.amaclinic.Entity.Secretaire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SecretaireRepository extends JpaRepository<Secretaire,Long >, JpaSpecificationExecutor<Secretaire> {
    Optional<Secretaire> findByCodeSecretaire(String CodeSecretaire);
    void deleteByCodeSecretaire(String CodeSecretaire);


}
