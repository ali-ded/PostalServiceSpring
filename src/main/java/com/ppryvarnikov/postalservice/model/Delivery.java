package com.ppryvarnikov.postalservice.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.annotations.Immutable;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "deliveries")
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@lombok.Builder(builderClassName = "Builder", toBuilder = true)
@lombok.Getter
@lombok.ToString
@Immutable
public class Delivery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_client", nullable = false)
    private Client client;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_department_sender", nullable = false)
    private Department departmentSender;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_department_recipient", nullable = false)
    private Department departmentRecipient;

    @Column(name = "recipient_phone", nullable = false)
    private Long recipientPhone;

    @Column(name = "recipient_surname", nullable = false, length = 30)
    private String recipientSurname;

    @Column(name = "recipient_first_name", nullable = false, length = 20)
    private String recipientFirstName;

    @Column(name = "recipient_patronymic", length = 30)
    private String recipientPatronymic;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_parcel_status", nullable = false)
    private ParcelStatus parcelStatus = ParcelStatus.builder()
            .id((short) 1)
            .build();

    @Column(name = "date_time_creation", nullable = false)
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss.SSS")
    private final LocalDateTime dateTimeCreation = LocalDateTime.now();

    @Column(name = "date_time_status_change")
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss.SSS")
    private LocalDateTime dateTimeStatusChange;
}
