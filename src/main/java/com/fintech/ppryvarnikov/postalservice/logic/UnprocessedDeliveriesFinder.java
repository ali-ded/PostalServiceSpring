package com.fintech.ppryvarnikov.postalservice.logic;

import com.fintech.ppryvarnikov.postalservice.model.Delivery;
import com.fintech.ppryvarnikov.postalservice.model.Notification;
import com.fintech.ppryvarnikov.postalservice.model.NotificationStatus;
import com.fintech.ppryvarnikov.postalservice.service.DeliveryService;
import com.fintech.ppryvarnikov.postalservice.service.NotificationService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

@Component
@Log4j2
public class UnprocessedDeliveriesFinder {
    private final DeliveryService deliveryService;
    private final NotificationService notificationService;

    @Autowired
    public UnprocessedDeliveriesFinder(DeliveryService deliveryService,
                                       NotificationService notificationService) {
        this.deliveryService = deliveryService;
        this.notificationService = notificationService;
    }

    @Scheduled(initialDelay = 5000, fixedDelay = 10000)
    public void run() {
        List<Delivery> unprocessedDeliveries = deliveryService.findUnprocessed();

        if (!unprocessedDeliveries.isEmpty()) {
            processDeliveries(unprocessedDeliveries);
        }
    }

    private void processDeliveries(List<Delivery> unprocessedDeliveries) {
        HashMap<Long, Future<Short>> processedDeliveries = new HashMap<>();

        ExecutorService parcelHandler = Executors.newCachedThreadPool();

        unprocessedDeliveries.forEach(delivery -> processedDeliveries.put(
                delivery.getId(),
                parcelHandler.submit(processParcel()))
        );

        saveToDatabase(processedDeliveries);

        parcelHandler.shutdown();
    }

    private Callable<Short> processParcel() {
        return () -> {
            Random random = new Random();
            ScheduledExecutorService scheduledReceiptOfParcel = Executors.newSingleThreadScheduledExecutor();
            short parcelStatusId = 3;

            for (int i = 0; i < 5; i++) {
                try {
                    boolean isParcelTaken = scheduledReceiptOfParcel.schedule(
                            () -> random.nextInt(5) == 0, 1, TimeUnit.SECONDS
                    ).get();

                    if (isParcelTaken) {
                        parcelStatusId = 2;
                        break;
                    }
                } catch (Exception e) {
                    log.error("An error occurred while trying to process the parcel: {}",
                            e.getMessage());
                }
            }

            scheduledReceiptOfParcel.shutdown();

            return parcelStatusId;
        };
    }

    private void saveToDatabase(HashMap<Long, Future<Short>> processedDeliveries) {
        processedDeliveries.forEach((deliveryId, parcelStatusId) -> {
            try {
                String message;

                if (parcelStatusId.get(6, TimeUnit.SECONDS) == 2) {
                    message = "Delivery with id " + deliveryId + " was successfully taken.";
                } else {
                    message = "Delivery with id " + deliveryId + " has been overdue.";
                }

                deliveryService.update(deliveryId, parcelStatusId.get(6, TimeUnit.SECONDS));

                notificationService.save(Notification.builder()
                        .message(message)
                        .notificationStatus(NotificationStatus.builder()
                                .id((short) 1)
                                .build())
                        .build());
                log.info("Delivery with id " + deliveryId + " successfully processed.");
            } catch (Exception e) {
                log.error("An error occurred while trying to update " +
                                "the status of a processed delivery with id {}: {}",
                        deliveryId,
                        e.getMessage());
            }
        });
    }
}
