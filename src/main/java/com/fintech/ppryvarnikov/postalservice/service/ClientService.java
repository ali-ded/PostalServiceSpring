package com.fintech.ppryvarnikov.postalservice.service;

import com.fintech.ppryvarnikov.postalservice.model.Client;

import javax.management.InstanceAlreadyExistsException;
import java.util.List;
import java.util.Optional;

public interface ClientService {
    Optional<Client> findById(Long id);
    Optional<Client> findByPhoneNumber(Long phoneNumber);
    List<Client> findAll();
    Client save(Client client) throws InstanceAlreadyExistsException;
    boolean update(Client client);
    boolean deleteById(Long id);
}
