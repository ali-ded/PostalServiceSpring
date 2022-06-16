package com.fintech.ppryvarnikov.postalservice.service;

import com.fintech.ppryvarnikov.postalservice.model.NotificationStatus;

import java.util.List;
import java.util.Optional;

public interface NotificationStatusService {
    Optional<NotificationStatus> findById(Short id);
    List<NotificationStatus> findAll();
    NotificationStatus save(NotificationStatus notificationStatus);
    boolean update(NotificationStatus notificationStatus);
    boolean deleteById(Short id);
}
