package com.ppryvarnikov.postalservice.service.implementation;

import com.ppryvarnikov.postalservice.model.ParcelStatus;
import com.ppryvarnikov.postalservice.repository.ParcelStatusRepository;
import com.ppryvarnikov.postalservice.service.ParcelStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ParcelStatusServiceImpl implements ParcelStatusService {
    private final ParcelStatusRepository parcelStatusRepository;

    @Autowired
    public ParcelStatusServiceImpl(ParcelStatusRepository parcelStatusRepository) {
        this.parcelStatusRepository = parcelStatusRepository;
    }

    @Override
    public Optional<ParcelStatus> findById(Short id) {
        return parcelStatusRepository.findById(id);
    }

    @Override
    public List<ParcelStatus> findAll() {
        return (List<ParcelStatus>) parcelStatusRepository.findAll();
    }

    @Override
    public ParcelStatus save(ParcelStatus parcelStatus) {
        return parcelStatusRepository.save(parcelStatus);
    }

    @Transactional
    @Override
    public boolean update(ParcelStatus parcelStatus) {
        boolean operationResult = false;
        int updatedRows;

        updatedRows = parcelStatusRepository.update(parcelStatus.getId(), parcelStatus.getStatus());

        if (updatedRows == 1) {
            operationResult = true;
        }

        return operationResult;
    }

    @Override
    public boolean deleteById(Short id) {
        boolean operationResult = false;

        if (parcelStatusRepository.findById(id).isPresent()) {
            parcelStatusRepository.deleteById(id);
            operationResult = true;
        }

        return operationResult;
    }
}
