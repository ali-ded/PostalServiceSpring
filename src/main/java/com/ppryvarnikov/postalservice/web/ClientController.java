package com.ppryvarnikov.postalservice.web;

import com.ppryvarnikov.postalservice.model.Client;
import com.ppryvarnikov.postalservice.service.ClientService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.management.InstanceAlreadyExistsException;
import javax.security.auth.login.LoginException;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.NoSuchElementException;

@Log4j2
@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
public class ClientController {
    private final ClientService clientService;

    @Autowired
    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @PostMapping("/registration")
    public String registration(@RequestBody Client clientDto) throws InstanceAlreadyExistsException {
        Client client = clientService.save(clientDto);
        log.info("New client registered successfully: {}", clientDto);
        return String.format("The new client with id %d was saved successfully.", client.getId());
    }

    @PostMapping("/login")
    public String login(@RequestBody Client clientDto, HttpSession session) throws LoginException {
        Client client = clientService
                .findByPhoneNumber(clientDto.getPhoneNumber())
                .orElseThrow(() -> new LoginException("No client found with this phone number."));
        session.setAttribute("clientId", client.getId());
        log.info("Client with id {} successfully logged in.", client.getId());
        return "You have successfully logged in.";
    }

    @GetMapping("/find-client-by-id")
    public Client findClientById(@RequestParam("id") Long id) throws NoSuchElementException {
        return clientService
                .findById(id)
                .orElseThrow(() -> new NoSuchElementException("Client with id number " + id + " not found.")
                );
    }

    @GetMapping("/find-all-clients")
    public List<Client> findAllClients() {
        return clientService.findAll();
    }
}
