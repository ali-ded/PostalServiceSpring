package com.fintech.ppryvarnikov.postalservice.service.implementation;

import com.fintech.ppryvarnikov.postalservice.model.Delivery;
import com.fintech.ppryvarnikov.postalservice.repository.DeliveryRepository;
import com.fintech.ppryvarnikov.postalservice.repository.DepartmentRepository;
import com.fintech.ppryvarnikov.postalservice.service.DeliveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DeliveryServiceImpl implements DeliveryService {
    private final DeliveryRepository deliveryRepository;
    private final DepartmentRepository departmentRepository;

    @Autowired
    public DeliveryServiceImpl(DeliveryRepository deliveryRepository, DepartmentRepository departmentRepository) {
        this.deliveryRepository = deliveryRepository;
        this.departmentRepository = departmentRepository;
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
        String illegalArguments = showIllegalArguments(delivery);
        if (!illegalArguments.isEmpty()) {
            throw new IllegalArgumentException("Unable to create a delivery because some fields are incorrect: " +
                    illegalArguments);
        }
        return deliveryRepository.save(delivery);
    }

    private String showIllegalArguments(Delivery delivery) {
        StringBuilder illegalArguments = new StringBuilder();
        if (delivery.getDepartmentSender() == null || delivery.getDepartmentSender().getId() == null ||
                departmentRepository.findById(delivery.getDepartmentSender().getId()).isEmpty()) {
            illegalArguments.append("'departmentSender' - there is no department for the given ID ");
        }
        if (delivery.getDepartmentRecipient() == null || delivery.getDepartmentRecipient().getId() == null ||
                departmentRepository.findById(delivery.getDepartmentRecipient().getId()).isEmpty()) {
            illegalArguments.append("'departmentRecipient' - there is no department for the given ID ");
        }
        if (delivery.getRecipientPhone() == null) {
            illegalArguments.append("'recipientPhone' - field must not be empty ");
        }
        if (delivery.getRecipientSurname() == null) {
            illegalArguments.append("'recipientSurname' - field must not be empty ");
        }
        if (delivery.getRecipientFirstName() == null) {
            illegalArguments.append("'recipientFirstName' - field must not be empty ");
        }
        return illegalArguments.toString();
    }

    @Override
    public List<Delivery> saveAll(List<Delivery> deliveries) {
        deliveries = deliveries.stream()
                .filter(delivery -> showIllegalArguments(delivery).isEmpty())
                .collect(Collectors.toList());
        if (deliveries.isEmpty()) {
            throw new IllegalArgumentException("None of the deliveries can be created because they are not filled correctly.");
        }
        return (List<Delivery>) deliveryRepository.saveAll(deliveries);
    }

    @Transactional
    @Override
    public boolean update(Long deliveryId, Short parcelStatusId) {
        boolean operationResult = false;
        int updatedRows;

        LocalDateTime currentDateTime = LocalDateTime.now();

        updatedRows = deliveryRepository.update(deliveryId,
                parcelStatusId,
                currentDateTime);

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
