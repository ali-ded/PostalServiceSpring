package com.fintech.ppryvarnikov.postalservice.service.implementation;

import com.fintech.ppryvarnikov.postalservice.model.NotificationStatus;
import com.fintech.ppryvarnikov.postalservice.repository.NotificationStatusRepository;
import com.fintech.ppryvarnikov.postalservice.service.NotificationStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class NotificationStatusServiceImpl implements NotificationStatusService {
    private final NotificationStatusRepository notificationStatusRepository;

    @Autowired
    public NotificationStatusServiceImpl(NotificationStatusRepository notificationStatusRepository) {
        this.notificationStatusRepository = notificationStatusRepository;
    }

    @Override
    public Optional<NotificationStatus> findById(Short id) {
        return notificationStatusRepository.findById(id);
    }

    @Override
    public List<NotificationStatus> findAll() {
        return (List<NotificationStatus>) notificationStatusRepository.findAll();
    }

    @Override
    public NotificationStatus save(NotificationStatus notificationStatus) {
        return notificationStatusRepository.save(notificationStatus);
    }

    @Transactional
    @Override
    public boolean update(NotificationStatus notificationStatus) {
        boolean operationResult = false;
        int updatedRows;

        updatedRows = notificationStatusRepository.update(notificationStatus.getId(), notificationStatus.getStatus());

        if (updatedRows == 1) {
            operationResult = true;
        }

        return operationResult;
    }

    @Override
    public boolean deleteById(Short id) {
        boolean operationResult = false;

        if (notificationStatusRepository.findById(id).isPresent()) {
            notificationStatusRepository.deleteById(id);
            operationResult = true;
        }

        return operationResult;
    }
}
