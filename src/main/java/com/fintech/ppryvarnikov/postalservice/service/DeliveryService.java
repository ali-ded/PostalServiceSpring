package com.fintech.ppryvarnikov.postalservice.service;

import com.fintech.ppryvarnikov.postalservice.model.Delivery;
import com.fintech.ppryvarnikov.postalservice.repository.DeliveryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class DeliveryService implements IDelivery {
    private final DeliveryRepository deliveryRepository;

    @Autowired
    public DeliveryService(DeliveryRepository deliveryRepository) {
        this.deliveryRepository = deliveryRepository;
    }

    @Override
    public Optional<Delivery> findById(Long id) {
        return deliveryRepository.findById(id);
    }

    @Override
    public List<Delivery> findAll() {
        return (List<Delivery>) deliveryRepository.findAll();
    }

    @Override
    public List<Delivery> findProcessed() {
        return deliveryRepository.findProcessed();
    }

    @Override
    public List<Delivery> findUnprocessed() {
        return (List<Delivery>) deliveryRepository.findUnprocessed();
    }

    @Override
    public Delivery save(Delivery delivery) {
        return deliveryRepository.save(delivery);
    }

    @Override
    public List<Delivery> saveAll(List<Delivery> deliveries) {
        return (List<Delivery>) deliveryRepository.saveAll(deliveries);
    }

    @Override
    public boolean update(Long deliveryId, Short parcelStatusId) {
        boolean operationResult = false;
        int updatedRows = 0;

        Timestamp currentTimestamp = Timestamp.valueOf(LocalDateTime.now());

        updatedRows = deliveryRepository.update(deliveryId,
                parcelStatusId,
                currentTimestamp);

        if (updatedRows == 1) {
            operationResult = true;
        }

        return operationResult;
    }

    @Override
    public boolean deleteById(Long id) {
        boolean operationResult = false;

        if (deliveryRepository.findById(id).isPresent()) {
            deliveryRepository.deleteById(id);
            operationResult = true;
        }

        return operationResult;
    }
}
