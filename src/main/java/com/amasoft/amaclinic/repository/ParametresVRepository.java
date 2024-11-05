package com.amasoft.amaclinic.repository;

import com.amasoft.amaclinic.Entity.ParametresVitaux;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ParametresVRepository extends JpaRepository<ParametresVitaux,Long>, JpaSpecificationExecutor<ParametresVitaux> {

    Optional<ParametresVitaux> findParametreByCodeParametre(String codeParametre);
    void deleteByCodeParametre(String codeParametre);




}

