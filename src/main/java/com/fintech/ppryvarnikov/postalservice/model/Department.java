package com.fintech.ppryvarnikov.postalservice.model;

import org.hibernate.annotations.Immutable;

import javax.persistence.*;

@Entity
@Table(name = "departments")
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@lombok.Builder(builderClassName = "Builder", toBuilder = true)
@lombok.Getter
@lombok.ToString
@Immutable
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String description;
}
