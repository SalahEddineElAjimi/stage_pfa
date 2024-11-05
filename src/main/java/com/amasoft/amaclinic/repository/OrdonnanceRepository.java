package com.amasoft.amaclinic.repository;

import com.amasoft.amaclinic.Entity.Ordonnance;
import com.amasoft.amaclinic.dto.MedicamentDetailsDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrdonnanceRepository extends JpaRepository<Ordonnance, Long>,JpaSpecificationExecutor<Ordonnance>{

        Optional<Ordonnance> findByCodeOrdonnance (String codeOrdonnance);

        @Modifying
        @Query("DELETE FROM Ordonnance or WHERE or.codeOrdonnance = :codeOrdonnance")
        void deleteByCodeOrdonnance(@Param("codeOrdonnance") String codeOrdonnance);


        @Query("SELECT new com.amasoft.amaclinic.dto.MedicamentDetailsDTO(m.nomMedicament, m.typeMedicament, od.dose, od.dureeTraitement, od.frequence, od.horaires) " +
                "FROM Ordonnance o " +
                "JOIN o.ordonnanceDetails od " +
                "JOIN od.medicament m " +
                "WHERE o.codeOrdonnance = :codeOrdonnance")
        List<MedicamentDetailsDTO> findMedicamentDetailsByCode(@Param("codeOrdonnance") String codeOrdonnance);



}
