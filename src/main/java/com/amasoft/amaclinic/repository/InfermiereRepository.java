package com.amasoft.amaclinic.repository;

import com.amasoft.amaclinic.Entity.Infermiere;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InfermiereRepository extends JpaRepository<Infermiere, Long>, JpaSpecificationExecutor<Infermiere> {


    Optional<Infermiere> findInfermiereByCodeInferm(String codeInferm);
    void deleteByCodeInferm(String codeInferm);
    List<Infermiere> findBySpecialiteId(Long specialiteId);
    Optional<Infermiere> findByCodeInferm(String codeInferm);
}

