package com.ppryvarnikov.postalservice.service;

import com.ppryvarnikov.postalservice.model.ParcelStatus;

import java.util.List;
import java.util.Optional;

public interface ParcelStatusService {
    Optional<ParcelStatus> findById(Short id);
    List<ParcelStatus> findAll();
    ParcelStatus save(ParcelStatus parcelStatus);
    boolean update(ParcelStatus parcelStatus);
    boolean deleteById(Short id);
}
