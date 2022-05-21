package com.fintech.ppryvarnikov.postalservice.model;

import org.hibernate.annotations.Immutable;

import javax.persistence.*;

@Entity
@Table(name = "clients")
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@lombok.Builder(builderClassName = "Builder", toBuilder = true)
@lombok.Getter
@lombok.ToString
@Immutable
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "surname", nullable = false, length = 30)
    private String surname;

    @Column(name = "first_name", nullable = false, length = 20)
    private String firstName;

    @Column(name = "patronymic", length = 30)
    private String patronymic;

    @Column(name = "email", length = 60)
    private String email;

    @Column (name = "phone_number", nullable = false, unique = true)
    private Long phoneNumber;
}
