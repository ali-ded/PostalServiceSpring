package com.fintech.ppryvarnikov.postalservice.service;

import com.fintech.ppryvarnikov.postalservice.model.Client;

import java.util.List;
import java.util.Optional;

public interface IClient {
    Optional<Client> findById(Long id);
    Optional<Client> findByPhoneNumber(Long phoneNumber);
    List<Client> findAll();
    Client save(Client client);
    boolean update(Client client);
    boolean deleteById(Long id);
}
