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
public class MedecinSpecialites {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "codeSpecialite", unique = true)
    private String codeSpecialite;
    @Column(name = "nomSpecialite")
    private String nomSpecialite ;

}
