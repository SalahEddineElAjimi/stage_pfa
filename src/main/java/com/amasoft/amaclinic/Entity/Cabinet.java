package com.amasoft.amaclinic.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Cabinet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codeCabinet", unique = true)
    private String codeCabinet;

    @Column(name = "nomCabinet")
    private String nomCabinet;

    @Column(name = "email")
    private String email;

    @Column(name = "adresse")
    private String adresse;

    @OneToMany(mappedBy = "cabinet", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Salle> salles;

    @ManyToMany
    @JoinTable(
            name = "cabinet_centre",
            joinColumns = @JoinColumn(name = "cabinet_id"),
            inverseJoinColumns = @JoinColumn(name = "centre_id")
    )
    private List<Centre> centres;

    public String getNom_cabinet() {
        return nomCabinet;
    }

    public void setNom_cabinet(String nom_cabinet) {
        this.nomCabinet = nom_cabinet;
    }


}
