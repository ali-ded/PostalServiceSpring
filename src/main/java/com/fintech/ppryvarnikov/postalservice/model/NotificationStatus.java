package com.fintech.ppryvarnikov.postalservice.model;

import org.hibernate.annotations.Immutable;

import javax.persistence.*;

@Entity
@Table(name = "notification_statuses")
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@lombok.Builder(builderClassName = "Builder", toBuilder = true)
@lombok.Getter
@lombok.ToString
@Immutable
public class NotificationStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Short id;

    @Column(nullable = false, unique = true, length = 30)
    private String status;
}
