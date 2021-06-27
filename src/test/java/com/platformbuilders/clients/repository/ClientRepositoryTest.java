package com.platformbuilders.clients.repository;

import com.platformbuilders.clients.model.Client;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataJpaTest
@DisplayName("Integration Tests")
class ClientRepositoryTest {

    @Autowired
    private ClientRepository clientRepository;

    @AfterEach
    void tearDown() {
        clientRepository.deleteAll();
    }

    @Test
    @DisplayName("Should save a new client and get by repository")
    void shouldSaveClient() {
        Client client = Client.builder()
                .name("Gabriel")
                .email("gabriel@gmail.com")
                .dateOfBirth(LocalDate.of(1992, 10, 27))
                .build();
        clientRepository.save(client);
        List<Client> all = clientRepository.findAll();
        assertThat(all.size() == 1).isTrue();

        Optional<Client> clientByOid = clientRepository.findClientByOid(all.get(0).getOid());
        assertThat(clientByOid.get().getOid().equals(client.getOid())).isTrue();
    }

    @Test
    @DisplayName("Should not return any client by oid")
    void shouldNotReturnAnyClientByOid() {
        Optional<Client> clientByOid = clientRepository.findClientByOid(UUID.randomUUID().toString());
        assertThat(clientByOid.isPresent()).isFalse();
    }

    @Test
    @DisplayName("Should check if client exists")
    void shouldCheckIfClientExistsEmail() {
        Client client = Client.builder()
                .name("Gabriel")
                .email("gabriel@gmail.com")
                .dateOfBirth(LocalDate.of(1992, 10, 27))
                .build();
        clientRepository.save(client);

        Boolean existsEmail = clientRepository.emailAlreadyExists("gabriel@gmail.com", UUID.randomUUID().toString());

        assertThat(existsEmail).isTrue();
    }

    @Test
    @DisplayName("Should check if email doest not exists")
    void shouldCheckIfEmailDoesNotExists() {
        Boolean existsEmail = clientRepository.emailAlreadyExists("gabriel@gmail.com", UUID.randomUUID().toString());
        assertThat(existsEmail).isFalse();
    }
}