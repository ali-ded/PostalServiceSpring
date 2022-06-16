package com.fintech.ppryvarnikov.postalservice.service.implementation;

import com.fintech.ppryvarnikov.postalservice.model.Notification;
import com.fintech.ppryvarnikov.postalservice.repository.NotificationRepository;
import com.fintech.ppryvarnikov.postalservice.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepository;

    @Autowired
    public NotificationServiceImpl(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Override
    public Optional<Notification> findById(Long id) {
        return notificationRepository.findById(id);
    }

    @Override
    public List<Notification> findAll() {
        return (List<Notification>) notificationRepository.findAll();
    }

    @Override
    public List<Notification> findUnprocessed() {
        return (List<Notification>) notificationRepository.findUnprocessed();
    }

    @Override
    public Notification save(Notification notification) {
        return notificationRepository.save(notification);
    }

    @Transactional
    @Override
    public boolean update(Notification notification) {
        boolean operationResult = false;
        int updatedRows;

        updatedRows = notificationRepository.update(notification.getId(),
                notification.getMessage(),
                notification.getNotificationStatus().getId());

        if (updatedRows == 1) {
            operationResult = true;
        }

        return operationResult;
    }

    @Override
    public boolean deleteById(Long id) {
        boolean operationResult = false;

        if (notificationRepository.findById(id).isPresent()) {
            notificationRepository.deleteById(id);
            operationResult = true;
        }

        return operationResult;
    }
}
