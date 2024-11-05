package com.amasoft.amaclinic.repository;

import com.amasoft.amaclinic.Entity.Equipement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface EquipementRepository extends JpaRepository<Equipement,Long> , JpaSpecificationExecutor<Equipement> {

    Optional<Equipement> findByCodeEquipement(String codeEquipement);
    List<Equipement> findByCodeEquipementIn(List<String> codeEquipements);

    void deleteByCodeEquipement(String codeEquipement);
}
