package com.fintech.ppryvarnikov.postalservice.logic;

import com.fintech.ppryvarnikov.postalservice.model.Notification;
import com.fintech.ppryvarnikov.postalservice.model.NotificationStatus;
import com.fintech.ppryvarnikov.postalservice.service.NotificationService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class UnprocessedNotificationsFinder {
    private final NotificationService notificationService;

    @Autowired
    public UnprocessedNotificationsFinder(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Scheduled(initialDelay = 10000, fixedDelay = 10000)
    public void run() {
        notificationService.findUnprocessed().forEach(this::processNotification);
    }

    private void processNotification(Notification notification) {
        notification = notification.toBuilder()
                .notificationStatus(NotificationStatus.builder()
                        .id((short) 2)
                        .build())
                .build();
        notificationService.update(notification);
        log.info("Notification with id {} successfully processed: \"{}\"",
                notification.getId(),
                notification.getMessage());
    }
}
