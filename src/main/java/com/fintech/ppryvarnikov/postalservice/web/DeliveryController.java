package com.fintech.ppryvarnikov.postalservice.web;

import com.fintech.ppryvarnikov.postalservice.model.Client;
import com.fintech.ppryvarnikov.postalservice.model.Delivery;
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
        if (session.isNew()) {
            session.invalidate();
            throw new AuthException("You are not authenticated. Log in please.");
        }

        Client client = clientService
                .findById((Long) session.getAttribute("clientId"))
                .orElseThrow(() -> new HttpSessionRequiredException("Session expired, please login."));

        delivery = delivery.toBuilder().client(client).build();

        deliveryService.save(delivery);
        log.info("New delivery created successfully: {}", delivery);

        unprocessedDeliveriesFinder.run();

        return delivery;
    }

    @PostMapping("/create-deliveries")
    public List<Delivery> createDeliveries(@RequestBody List<Delivery> deliveries, HttpSession session) throws AuthException, HttpSessionRequiredException {
        if (session.isNew()) {
            session.invalidate();
            throw new AuthException("You are not authenticated. Log in please.");
        }

        Client client = clientService
                .findById((Long) session.getAttribute("clientId"))
                .orElseThrow(() -> new HttpSessionRequiredException("Session expired, please login."));

        deliveries = deliveries.stream()
                .map(delivery -> delivery.toBuilder().client(client).build())
                .collect(Collectors.toList());

        deliveryService.saveAll(deliveries);
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
