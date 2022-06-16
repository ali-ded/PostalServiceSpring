package com.ppryvarnikov.postalservice.service.implementation;

import com.ppryvarnikov.postalservice.model.Client;
import com.ppryvarnikov.postalservice.repository.ClientRepository;
import com.ppryvarnikov.postalservice.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.management.InstanceAlreadyExistsException;
import java.util.List;
import java.util.Optional;

@Service
public class ClientServiceImpl implements ClientService {
    private final ClientRepository clientRepository;

    @Autowired
    public ClientServiceImpl(ClientRepository clientRepository) {
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
    public Client save(Client client) throws InstanceAlreadyExistsException {
        String illegalArguments = showIllegalArguments(client);
        if (!illegalArguments.isEmpty()) {
            throw new IllegalArgumentException("The following fields cannot be null: " + illegalArguments);
        }
        if (clientRepository.findByPhoneNumber(client.getPhoneNumber()).isPresent()) {
            throw new InstanceAlreadyExistsException("A client with this phone number already exists.");
        }
        return clientRepository.save(client);
    }

    private String showIllegalArguments(Client client) {
        StringBuilder stringBuilder = new StringBuilder();
        if (client.getSurname() == null) {
            stringBuilder.append("'surname' ");
        }
        if (client.getFirstName() == null) {
            stringBuilder.append("'firstName' ");
        }
        if (client.getPhoneNumber() == null) {
            stringBuilder.append("'phoneNumber'");
        }
        return stringBuilder.toString();
    }

    @Transactional
    @Override
    public boolean update(Client client) {
        boolean operationResult = false;
        int updatedRows;

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
