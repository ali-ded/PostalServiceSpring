package com.ppryvarnikov.postalservice.service;

import com.ppryvarnikov.postalservice.model.Client;
import com.ppryvarnikov.postalservice.repository.ClientRepository;
import com.ppryvarnikov.postalservice.service.implementation.ClientServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.management.InstanceAlreadyExistsException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClientServiceTest {
    private ClientRepository clientRepository;
    private ClientServiceImpl clientService;

    @BeforeEach
    void setUp() {
        clientRepository = Mockito.mock(ClientRepository.class);
        clientService = new ClientServiceImpl(clientRepository);
    }

    @ParameterizedTest
    @MethodSource("getClients")
    void findByIdExists(List<Client> existingClients) {
        Long searchId = 3L;

        doReturn(existingClients.stream()
                .filter(currentClient -> currentClient.getId().equals(searchId))
                .findFirst()
        ).when(clientRepository).findById(searchId);

        Client clientFound = clientService.findById(searchId).orElse(new Client());

        verify(clientRepository, times(1)).findById(searchId);

        assertThat(existingClients.contains(clientFound)).isTrue();
        assertThat(clientFound.getId()).isEqualTo(3L);
        assertThat(clientFound.getSurname()).isEqualTo("Williams");
        assertThat(clientFound.getFirstName()).isEqualTo("John");
        assertThat(clientFound.getPatronymic()).isEqualTo(null);
        assertThat(clientFound.getEmail()).isEqualTo("jwilliams@gmail.com");
        assertThat(clientFound.getPhoneNumber()).isEqualTo(634951038L);
    }

    @ParameterizedTest
    @MethodSource("getClients")
    void findByIdNotExists(List<Client> existingClients) {
        Long searchId = 111L;

        doReturn(existingClients.stream()
                .filter(currentClient -> currentClient.getId().equals(searchId))
                .findFirst()
        ).when(clientRepository).findById(searchId);

        Client clientFound = clientService.findById(searchId).orElse(new Client());

        verify(clientRepository, times(1)).findById(searchId);

        assertThat(existingClients.contains(clientFound)).isFalse();
        assertThat(clientFound.getId()).isEqualTo(null);
        assertThat(clientFound.getSurname()).isEqualTo(null);
        assertThat(clientFound.getFirstName()).isEqualTo(null);
        assertThat(clientFound.getPatronymic()).isEqualTo(null);
        assertThat(clientFound.getEmail()).isEqualTo(null);
        assertThat(clientFound.getPhoneNumber()).isEqualTo(null);
    }

    @ParameterizedTest
    @MethodSource("getClients")
    void findByPhoneNumberExists(List<Client> existingClients) {
        Long searchPhoneNumber = 994583672L;

        doReturn(existingClients.stream()
                .filter(currentPhoneNumber -> currentPhoneNumber.getPhoneNumber().equals(searchPhoneNumber))
                .findFirst()
        ).when(clientRepository).findByPhoneNumber(searchPhoneNumber);

        Client clientFound = clientService.findByPhoneNumber(searchPhoneNumber).orElse(new Client());

        verify(clientRepository, times(1)).findByPhoneNumber(searchPhoneNumber);

        assertThat(existingClients.contains(clientFound)).isTrue();
        assertThat(clientFound.getId()).isEqualTo(1L);
        assertThat(clientFound.getSurname()).isEqualTo("Smith");
        assertThat(clientFound.getFirstName()).isEqualTo("James");
        assertThat(clientFound.getPatronymic()).isEqualTo(null);
        assertThat(clientFound.getEmail()).isEqualTo("jsmith@gmail.com");
        assertThat(clientFound.getPhoneNumber()).isEqualTo(994583672L);
    }

    @ParameterizedTest
    @MethodSource("getClients")
    void findByPhoneNumberNotExists(List<Client> existingClients) {
        Long searchPhoneNumber = 671234567L;

        doReturn(existingClients.stream()
                .filter(currentPhoneNumber -> currentPhoneNumber.getPhoneNumber().equals(searchPhoneNumber))
                .findFirst()
        ).when(clientRepository).findByPhoneNumber(searchPhoneNumber);

        Client clientFound = clientService.findByPhoneNumber(searchPhoneNumber).orElse(new Client());

        verify(clientRepository, times(1)).findByPhoneNumber(searchPhoneNumber);

        assertThat(existingClients.contains(clientFound)).isFalse();
        assertThat(clientFound.getId()).isEqualTo(null);
        assertThat(clientFound.getSurname()).isEqualTo(null);
        assertThat(clientFound.getFirstName()).isEqualTo(null);
        assertThat(clientFound.getPatronymic()).isEqualTo(null);
        assertThat(clientFound.getEmail()).isEqualTo(null);
        assertThat(clientFound.getPhoneNumber()).isEqualTo(null);
    }

    @ParameterizedTest
    @MethodSource("getClients")
    void findAllFromFilledList(List<Client> existingClients) {
        doReturn(existingClients).when(clientRepository).findAll();

        List<Client> receivedListOfClients = clientService.findAll();

        verify(clientRepository, times(1)).findAll();

        assertThat(existingClients.equals(receivedListOfClients)).isTrue();
    }

    @Test
    void findAllFromEmptyList() {
        List<Client> receivedListOfClients = clientService.findAll();

        verify(clientRepository, times(1)).findAll();

        assertThat(receivedListOfClients.isEmpty()).isTrue();
    }

    @ParameterizedTest
    @MethodSource("getClients")
    void saveClientWithNewPhoneNumber(List<Client> existingClients) {
        Client testClient = Client.builder()
                .surname("Brown")
                .firstName("Patricia")
                .patronymic("Petrovna")
                .email("pbrown@gmail.com")
                .phoneNumber(503490567L)
                .build();

        doReturn(existingClients.stream()
                .filter(currentClient -> currentClient.getPhoneNumber().equals(testClient.getPhoneNumber()))
                .findFirst()
        ).when(clientRepository).findByPhoneNumber(testClient.getPhoneNumber());

        doReturn(Client.builder()
                .id(4L)
                .surname("Brown")
                .firstName("Patricia")
                .patronymic("Petrovna")
                .email("pbrown@gmail.com")
                .phoneNumber(503490567L)
                .build())
                .when(clientRepository).save(eq(testClient));

        Client client;
        try {
            client = clientService.save(testClient);
        } catch (InstanceAlreadyExistsException e) {
            client = new Client();
        }

        verify(clientRepository, times(1)).findByPhoneNumber(eq(testClient.getPhoneNumber()));
        verify(clientRepository, times(1)).save(eq(testClient));

        assertThat(existingClients.contains(client)).isFalse();
        assertThat(client.getId()).isEqualTo(4L);
        assertThat(client.getSurname()).isEqualTo("Brown");
        assertThat(client.getFirstName()).isEqualTo("Patricia");
        assertThat(client.getPatronymic()).isEqualTo("Petrovna");
        assertThat(client.getEmail()).isEqualTo("pbrown@gmail.com");
        assertThat(client.getPhoneNumber()).isEqualTo(503490567L);
    }

    @ParameterizedTest
    @MethodSource("getClients")
    void saveClientWithExistingPhoneNumber(List<Client> existingClients) {
        Client testClient = Client.builder()
                .surname("Miller")
                .firstName("Robert")
                .email("rmiller@gmail.com")
                .phoneNumber(670653491L)
                .build();

        doReturn(existingClients.stream()
                .filter(currentClient -> currentClient.getPhoneNumber().equals(testClient.getPhoneNumber()))
                .findFirst()
        ).when(clientRepository).findByPhoneNumber(testClient.getPhoneNumber());

        Client client;
        try {
            client = clientService.save(testClient);
        } catch (InstanceAlreadyExistsException e) {
            client = new Client();
        }

        verify(clientRepository, times(1)).findByPhoneNumber(eq(testClient.getPhoneNumber()));
        verify(clientRepository, times(0)).save(any());

        assertThat(client.getId()).isEqualTo(null);
        assertThat(client.getSurname()).isEqualTo(null);
        assertThat(client.getFirstName()).isEqualTo(null);
        assertThat(client.getPatronymic()).isEqualTo(null);
        assertThat(client.getEmail()).isEqualTo(null);
        assertThat(client.getPhoneNumber()).isEqualTo(null);
    }

    @ParameterizedTest
    @MethodSource("getClients")
    void updateExists(List<Client> existingClients) {
        Client testClient = Client.builder()
                .id(1L)
                .surname("Garcia")
                .firstName("Elizabeth")
                .email("egarcia@gmail.com")
                .phoneNumber(936087436L)
                .build();

        boolean isIdFound = existingClients.stream()
                .anyMatch(currentClient -> currentClient.getId().equals(testClient.getId()));
        int updatedRows = isIdFound ? 1 : 0;

        doReturn(updatedRows).when(clientRepository).update(
                testClient.getId(),
                testClient.getSurname(),
                testClient.getFirstName(),
                testClient.getPatronymic(),
                testClient.getEmail(),
                testClient.getPhoneNumber());

        boolean updateResult = clientService.update(testClient);

        verify(clientRepository, times(1)).update(
                testClient.getId(),
                testClient.getSurname(),
                testClient.getFirstName(),
                testClient.getPatronymic(),
                testClient.getEmail(),
                testClient.getPhoneNumber()
        );

        assertThat(updateResult).isTrue();
    }

    @ParameterizedTest
    @MethodSource("getClients")
    void updateNotExists(List<Client> existingClients) {
        Client testClient = Client.builder()
                .id(123L)
                .surname("Garcia")
                .firstName("Elizabeth")
                .email("egarcia@gmail.com")
                .phoneNumber(936087436L)
                .build();

        boolean isIdFound = existingClients.stream()
                .anyMatch(currentClient -> currentClient.getId().equals(testClient.getId()));
        int updatedRows = isIdFound ? 1 : 0;

        doReturn(updatedRows).when(clientRepository).update(
                testClient.getId(),
                testClient.getSurname(),
                testClient.getFirstName(),
                testClient.getPatronymic(),
                testClient.getEmail(),
                testClient.getPhoneNumber());

        boolean updateResult = clientService.update(testClient);

        verify(clientRepository, times(1)).update(
                testClient.getId(),
                testClient.getSurname(),
                testClient.getFirstName(),
                testClient.getPatronymic(),
                testClient.getEmail(),
                testClient.getPhoneNumber()
        );

        assertThat(updateResult).isFalse();
    }

    @ParameterizedTest
    @MethodSource("getClients")
    void deleteByIdExists(List<Client> existingClients) {
        Long testId = 2L;

        doReturn(existingClients.stream()
                .filter(currentClient -> currentClient.getId().equals(testId))
                .findFirst()
        ).when(clientRepository).findById(testId);

        doNothing().when(clientRepository).deleteById(testId);

        boolean operationResult = clientService.deleteById(testId);

        verify(clientRepository, times(1)).findById(testId);
        verify(clientRepository, times(1)).deleteById(testId);

        assertThat(operationResult).isTrue();
    }

    @ParameterizedTest
    @MethodSource("getClients")
    void deleteByIdNotExists(List<Client> existingClients) {
        Long testId = 4L;

        doReturn(existingClients.stream()
                .filter(currentClient -> currentClient.getId().equals(testId))
                .findFirst()
        ).when(clientRepository).findById(testId);

        boolean operationResult = clientService.deleteById(testId);

        verify(clientRepository, times(1)).findById(testId);
        verify(clientRepository, times(0)).deleteById(testId);

        assertThat(operationResult).isFalse();
    }

    private static Stream<Arguments> getClients() {
        return Stream.of(Arguments.of(Stream.of(
                Client.builder()
                        .id(1L)
                        .surname("Smith")
                        .firstName("James")
                        .email("jsmith@gmail.com")
                        .phoneNumber(994583672L)
                        .build(),
                Client.builder()
                        .id(2L)
                        .surname("Johnson")
                        .firstName("Mary")
                        .email("mjohnson@gmail.com")
                        .phoneNumber(670653491L)
                        .build(),
                Client.builder()
                        .id(3L)
                        .surname("Williams")
                        .firstName("John")
                        .email("jwilliams@gmail.com")
                        .phoneNumber(634951038L)
                        .build()
        ).collect(Collectors.toList())));
    }
}