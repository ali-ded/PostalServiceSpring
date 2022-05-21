package com.fintech.ppryvarnikov.postalservice.web.logic;

import com.fintech.ppryvarnikov.postalservice.model.Delivery;
import com.fintech.ppryvarnikov.postalservice.model.Notification;
import com.fintech.ppryvarnikov.postalservice.model.NotificationStatus;
import com.fintech.ppryvarnikov.postalservice.service.DeliveryService;
import com.fintech.ppryvarnikov.postalservice.service.NotificationService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

@Component
@Log4j2
public class UnprocessedDeliveriesFinder {
    private final DeliveryService deliveryService;
    private final NotificationService notificationService;
    @Autowired
    private UnprocessedNotificationsFinder unprocessedNotificationsFinder;

    @Autowired
    public UnprocessedDeliveriesFinder(DeliveryService deliveryService,
                                       NotificationService notificationService) {
        this.deliveryService = deliveryService;
        this.notificationService = notificationService;
    }

    @Async
    public void run() {
        List<Delivery> deliveries;
        Iterator<Delivery> iterator;

        deliveries = deliveryService.findUnprocessed();
        iterator = deliveries.listIterator();
        while (iterator.hasNext()) {
            processDelivery(iterator.next());
            iterator.remove();
        }

        unprocessedNotificationsFinder.run();
    }

    @Async
    private void processDelivery(Delivery delivery) {
        Random random = new Random();
        String message = null;
        boolean probablyParcelTaken;
        short parcelStatusId = 3;

        for (int i = 0; i < 5; i++) {
            probablyParcelTaken = random.nextInt(5) == 0;
            if (probablyParcelTaken) {
                parcelStatusId = 2;
                message = "Delivery with id " + delivery.getId() + " was successfully taken.";
                break;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                log.error(e.getMessage());
            }
        }

        if (parcelStatusId == 3) {
            message = "Delivery with id " + delivery.getId() + " has been overdue.";
        }

        deliveryService.update(delivery.getId(), parcelStatusId);
        notificationService.save(Notification.builder()
                .message(message)
                .notificationStatus(NotificationStatus.builder()
                        .id((short) 1)
                        .build())
                .build());
        log.info("Delivery with id " + delivery.getId() + " successfully processed.");
    }
}
