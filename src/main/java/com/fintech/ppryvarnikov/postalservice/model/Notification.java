package com.fintech.ppryvarnikov.postalservice.model;

import org.hibernate.annotations.Immutable;

import javax.persistence.*;

@Entity
@Table(name = "notifications")
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@lombok.Builder(builderClassName = "Builder", toBuilder = true)
@lombok.Getter
@lombok.ToString
@Immutable
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String message;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_status", nullable = false)
    private NotificationStatus notificationStatus = NotificationStatus.builder()
            .id((short) 1)
            .build();
}
