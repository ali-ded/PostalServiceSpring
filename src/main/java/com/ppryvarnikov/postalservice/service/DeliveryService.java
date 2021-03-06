package com.ppryvarnikov.postalservice.service;

import com.ppryvarnikov.postalservice.model.Delivery;

import java.util.List;
import java.util.Optional;

public interface DeliveryService {
    Optional<Delivery> findById(Long id);
    List<Delivery> findAll();
    List<Delivery> findProcessed();
    List<Delivery> findUnprocessed();
    Delivery save(Delivery delivery);
    List<Delivery> saveAll(List<Delivery> deliveries);
    boolean update(Long deliveryId, Short parcelStatusId);
    boolean deleteById(Long id);
}
