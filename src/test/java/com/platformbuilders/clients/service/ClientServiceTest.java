package com.platformbuilders.clients.service;

import com.platformbuilders.clients.exception.ClientNotFoundException;
import com.platformbuilders.clients.exception.ClientValidationException;
import com.platformbuilders.clients.model.Client;
import com.platformbuilders.clients.repository.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@DisplayName("Client Service Test")
class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;

    private ClientService clientService;

    @BeforeEach
    void setUp() {
        clientService = new ClientService(clientRepository);
    }

    @Test
    @DisplayName("Should get all clients")
    void shouldGetAllClients() {
        clientService.findAllClients();
        verify(clientRepository).findAll();
    }

    @Test
    @DisplayName("Should save a new client")
    void shouldSaveNewClient() {
        Client client = Client.builder()
                .name("Gabriel")
                .email("gabriel@gmail.com")
                .dateOfBirth(LocalDate.of(1992, 10, 27))
                .build();

        clientService.save(client);

        ArgumentCaptor<Client> clientCaptor = ArgumentCaptor.forClass(Client.class);

        verify(clientRepository).save(clientCaptor.capture());

        Client capturedClient = clientCaptor.getValue();
        assertThat(capturedClient).isEqualTo(client);
    }

    @Test
    @DisplayName("Should return a required name exception")
    void shouldReturnNameValidationException() {
        assertThatThrownBy(() -> {
            Client client = new Client();
            clientService.save(client);
        }).isInstanceOf(ClientValidationException.class).hasMessageContaining("Enter a name");
    }

    @Test
    @DisplayName("Should return a required email exception")
    void shouldReturnEmailValidationException() {
        assertThatThrownBy(() -> {
            Client client = new Client();
            client.setName("Gabriel");
            clientService.save(client);
        }).isInstanceOf(ClientValidationException.class).hasMessageContaining("Enter an email");
    }

    @Test
    @DisplayName("Should return an invalid email exception")
    void shouldReturnInvalidEmailException() {
        assertThatThrownBy(() -> {
            Client client = new Client();
            client.setName("Gabriel");
            client.setEmail("Gabriel");
            clientService.save(client);
        }).isInstanceOf(ClientValidationException.class).hasMessageContaining("Email is not valid");
    }

    @Test
    @DisplayName("Should return a required date exception")
    void shouldReturnRequiredDateException() {
        assertThatThrownBy(() -> {
            Client client = new Client();
            client.setName("Gabriel");
            client.setEmail("gabriel.testoni@gmail.com");
            clientService.save(client);
        }).isInstanceOf(ClientValidationException.class).hasMessageContaining("Enter a date of birth");
    }

    @Test
    @DisplayName("Should not found client to delete")
    void shouldNotFoundClientToDelete() {
        assertThatThrownBy(() -> {
            clientService.delete("testoID");
        }).isInstanceOf(ClientNotFoundException.class).hasMessageContaining("Client does not exists");
    }

    @Test
    @DisplayName("Should not found client by oid")
    void shouldNotFoundClientByOid() {
        assertThatThrownBy(() -> {
            clientService.findClientByOid("testoID");
        }).isInstanceOf(ClientNotFoundException.class).hasMessageContaining("Client does not exists");
    }
}