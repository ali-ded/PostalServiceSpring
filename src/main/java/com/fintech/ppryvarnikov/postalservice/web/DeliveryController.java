package com.fintech.ppryvarnikov.postalservice.web;

import com.fintech.ppryvarnikov.postalservice.model.Client;
import com.fintech.ppryvarnikov.postalservice.model.Delivery;
import com.fintech.ppryvarnikov.postalservice.model.ParcelStatus;
import com.fintech.ppryvarnikov.postalservice.service.ClientService;
import com.fintech.ppryvarnikov.postalservice.service.DeliveryService;
import com.fintech.ppryvarnikov.postalservice.web.logic.UnprocessedDeliveriesFinder;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.HttpSessionRequiredException;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.message.AuthException;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Log4j2
@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
public class DeliveryController {
    private final DeliveryService deliveryService;
    private final ClientService clientService;
    @Autowired
    private UnprocessedDeliveriesFinder unprocessedDeliveriesFinder;

    @Autowired
    public DeliveryController(DeliveryService deliveryService, ClientService clientService) {
        this.deliveryService = deliveryService;
        this.clientService = clientService;
    }

    @PostMapping("/create-delivery")
    public Delivery createDelivery(@RequestBody Delivery delivery, HttpSession session) throws AuthException, HttpSessionRequiredException {
        Client client = getClientByHttpSession(session);
        delivery = delivery.toBuilder()
                .parcelStatus(ParcelStatus.builder()
                        .id((short) 1)
                        .build())
                .client(client)
                .build();
        delivery = deliveryService.save(delivery);
        log.info("New delivery created successfully: {}", delivery);
        delivery = deliveryService.findById(delivery.getId()).orElseThrow();
        unprocessedDeliveriesFinder.run();
        return delivery;
    }

    private Client getClientByHttpSession(HttpSession session) throws AuthException, HttpSessionRequiredException {
        Long clientId = (Long) session.getAttribute("clientId");
        if (clientId == null) {
            throw new AuthException("You are not authenticated. Log in please.");
        }

        return (clientService
                .findById(clientId)
                .orElseThrow(() -> new HttpSessionRequiredException("Session expired, please login."))
        );
    }

    @PostMapping("/create-deliveries")
    public List<Delivery> createDeliveries(@RequestBody List<Delivery> deliveries, HttpSession session) throws AuthException, HttpSessionRequiredException {
        Client client = getClientByHttpSession(session);
        deliveries = deliveries.stream()
                .map(delivery -> delivery.toBuilder()
                        .parcelStatus(ParcelStatus.builder()
                                .id((short) 1)
                                .build())
                        .client(client).build())
                .collect(Collectors.toList());
        deliveries = deliveryService.saveAll(deliveries);
        deliveries = deliveries.stream()
                .map(delivery -> delivery = deliveryService.findById(delivery.getId()).orElseThrow())
                .collect(Collectors.toList());
        log.info("New deliveries created: {}", deliveries.size());
        unprocessedDeliveriesFinder.run();
        return deliveries;
    }

    @GetMapping("/find-delivery-by-id")
    public Delivery findDeliveryById(@RequestParam("id") Long id) {
        return deliveryService
                .findById(id)
                .orElseThrow(() -> new NoSuchElementException("Delivery with id number " + id + " not found.")
                );
    }

    @GetMapping("/find-all-deliveries")
    public List<Delivery> findAllDeliveries() {
        return deliveryService.findAll();
    }
}
