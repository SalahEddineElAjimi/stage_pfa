package com.amasoft.amaclinic.Entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrdonnanceDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codeOrdonnanceDetails", unique = true)
    private String codeOrdonnanceDetails;

    @Column(name = "dose")
    private String dose;

    @Column(name = "horaires")
    private String horaires ;

    @Column(name = "dureeTraitement")
    private Integer dureeTraitement; // Durée du traitement en jours

    @Column(name = "instructionsSpeciales")
    private String instructionsSpeciales; // Instructions spécifiques pour le médicament

    @Column(name = "frequence")
    private Integer frequence; // Nombre de fois que le médicament doit être pris par jour

    @ManyToOne
    @JoinColumn(name = "ordonnance_id")
    private Ordonnance ordonnance;

    @ManyToOne
    @JoinColumn(name = "medicament_id")
    private Medicament medicament;

}
