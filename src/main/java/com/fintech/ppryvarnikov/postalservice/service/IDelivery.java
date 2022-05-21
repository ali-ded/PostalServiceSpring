package com.fintech.ppryvarnikov.postalservice.service;

import com.fintech.ppryvarnikov.postalservice.model.Delivery;

import java.util.List;
import java.util.Optional;

public interface IDelivery {
    Optional<Delivery> findById(Long id);
    List<Delivery> findAll();
    List<Delivery> findProcessed();
    List<Delivery> findUnprocessed();
    Delivery save(Delivery delivery);
    List<Delivery> saveAll(List<Delivery> deliveries);
    boolean update(Long deliveryId, Short parcelStatusId);
    boolean deleteById(Long id);
}
