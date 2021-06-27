package com.platformbuilders.clients.service;

import com.platformbuilders.clients.exception.ClientNotFoundException;
import com.platformbuilders.clients.exception.ClientValidationException;
import com.platformbuilders.clients.model.Client;
import com.platformbuilders.clients.repository.ClientRepository;
import com.platformbuilders.clients.util.StringUtils;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    public List<Client> findAllClients() {
        return clientRepository.findAll();
    }

    @Transactional
    public Client save(Client client) {
        validateClient(client);
        return clientRepository.save(client);
    }

    private void validateClient(Client client) {
        if (StringUtils.isEmptyString(client.getName())) {
            throw new ClientValidationException("Enter a name");
        }

        if (StringUtils.isEmptyString(client.getEmail())) {
            throw new ClientValidationException("Enter an email");
        } else if (!StringUtils.isValidEmailAddress(client.getEmail())) {
            throw new ClientValidationException("Email is not valid");
        }

        if (client.getDateOfBirth() == null) {
            throw new ClientValidationException("Enter a date of birth");
        }

        if (clientRepository.emailAlreadyExists(client.getEmail(), client.getOid())) {
            throw new ClientValidationException("Email already exists");
        }
    }

    @Transactional
    public void delete(String clientOid) {
        Optional<Client> clientByOid = clientRepository.findClientByOid(clientOid);
        if (!clientByOid.isPresent()) {
            throw new ClientNotFoundException("Client does not exists");
        }

        clientRepository.delete(clientByOid.get());
    }

    public Client findClientByOid(String oid) {
        Optional<Client> clientByOid = clientRepository.findClientByOid(oid);
        if (!clientByOid.isPresent()) {
            throw new ClientNotFoundException("Client does not exists");
        }

        return clientByOid.get();
    }
}
