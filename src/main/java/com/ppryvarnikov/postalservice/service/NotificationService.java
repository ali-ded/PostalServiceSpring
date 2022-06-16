package com.ppryvarnikov.postalservice.service;

import com.ppryvarnikov.postalservice.model.Notification;

import java.util.List;
import java.util.Optional;

public interface NotificationService {
    Optional<Notification> findById(Long id);
    List<Notification> findAll();
    List<Notification> findUnprocessed();
    Notification save(Notification notification);
    boolean update(Notification notification);
    boolean deleteById(Long id);
}
