package com.amasoft.amaclinic.repository;

import com.amasoft.amaclinic.Entity.Medicament;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MedicamentRepository extends JpaRepository<Medicament, Long>,JpaSpecificationExecutor<Medicament>{
    
        Optional<Medicament> findByCodeMedicament (String codeMedicament);
        void deleteByCodeMedicament (String codeMedicament);

    
}
