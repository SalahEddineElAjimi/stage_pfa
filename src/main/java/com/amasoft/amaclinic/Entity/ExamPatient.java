package com.amasoft.amaclinic.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExamPatient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id ;

    @Column(name = "codeExamPatient" , unique = true)
    private String codeExamPatient ;

    @Column(name = "date")
    private LocalDate dateExam ;
    @Column(name = "detail")
    private String detail ;
    @Column(name = "type")
    private String type ;

    @Transient
    private String codePatient ;

    @ManyToOne
    private Patient patient;

}
