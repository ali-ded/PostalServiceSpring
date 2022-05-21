package com.fintech.ppryvarnikov.postalservice.service;

import com.fintech.ppryvarnikov.postalservice.model.Client;
import com.fintech.ppryvarnikov.postalservice.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ClientService implements IClient {
    private final ClientRepository clientRepository;

    @Autowired
    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public Optional<Client> findById(Long id) {
        return clientRepository.findById(id);
    }

    @Override
    public Optional<Client> findByPhoneNumber(Long phoneNumber) {
        return clientRepository.findByPhoneNumber(phoneNumber);
    }

    @Override
    public List<Client> findAll() {
        return (List<Client>) clientRepository.findAll();
    }

    @Override
    public Client save(Client client) {
        return clientRepository.save(client);
    }

    @Override
    public boolean update(Client client) {
        boolean operationResult = false;
        int updatedRows = 0;

        updatedRows = clientRepository.update(client.getId(),
                client.getSurname(),
                client.getFirstName(),
                client.getPatronymic(),
                client.getEmail(),
                client.getPhoneNumber());

        if (updatedRows == 1) {
            operationResult = true;
        }

        return operationResult;
    }

    @Override
    public boolean deleteById(Long id) {
        boolean operationResult = false;

        if (clientRepository.findById(id).isPresent()) {
            clientRepository.deleteById(id);
            operationResult = true;
        }

        return operationResult;
    }
}
