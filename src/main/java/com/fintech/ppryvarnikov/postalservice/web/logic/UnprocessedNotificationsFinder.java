package com.fintech.ppryvarnikov.postalservice.web.logic;

import com.fintech.ppryvarnikov.postalservice.model.Notification;
import com.fintech.ppryvarnikov.postalservice.model.NotificationStatus;
import com.fintech.ppryvarnikov.postalservice.service.NotificationService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.List;

@Component
@Log4j2
public class UnprocessedNotificationsFinder {
    private final NotificationService notificationService;

    @Autowired
    public UnprocessedNotificationsFinder(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Async
    public void run() {
        List<Notification> notifications;
        Iterator<Notification> iterator;

        notifications = notificationService.findUnprocessed();
        iterator = notifications.listIterator();
        while (iterator.hasNext()) {
            processNotification(iterator.next());
            iterator.remove();
        }
    }

    @Async
    private void processNotification(Notification notification) {
        notification = notification.toBuilder()
                .notificationStatus(NotificationStatus.builder()
                        .id((short) 2)
                        .build())
                .build();
        notificationService.update(notification);
        log.info(String.format("Notification with id %d successfully processed. %s",
                notification.getId(),
                notification.getMessage()));
    }
}
