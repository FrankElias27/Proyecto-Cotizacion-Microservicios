package com.programming.frank.client_microservice.service;

import com.programming.frank.client_microservice.dto.ClientRequest;
import com.programming.frank.client_microservice.dto.ClientResponse;
import com.programming.frank.client_microservice.model.Client;
import com.programming.frank.client_microservice.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClientService {

    private final ClientRepository clientRepository;

    public void addClient(ClientRequest clientRequest) {
        var client = Client.builder()
                .firstName(clientRequest.getFirstName())
                .lastName(clientRequest.getLastName())
                .dni(clientRequest.getDni())
                .email(clientRequest.getEmail())
                .phone(clientRequest.getPhone())
                .address(clientRequest.getAddress())
                .build();

        clientRepository.save(client);

        log.info("Client added: {}", client);
    }

    public List<ClientResponse> getAllClients() {
        var clients = clientRepository.findAll();

        log.info("Retrieved {} clients", clients.size());

        return clients.stream().map(this::mapToClientResponse).toList();
    }


    public void updateClient(Long clientId, ClientRequest clientRequest) {
        var existingClient = clientRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Client not found with ID: " + clientId));

        existingClient.setFirstName(clientRequest.getFirstName());
        existingClient.setLastName(clientRequest.getLastName());
        existingClient.setDni(clientRequest.getDni());
        existingClient.setEmail(clientRequest.getEmail());
        existingClient.setPhone(clientRequest.getPhone());
        existingClient.setAddress(clientRequest.getAddress());

        clientRepository.save(existingClient);

        log.info("Client updated: {}", existingClient);
    }

    public void deleteClient(Long clientId) {
        var existingClient = clientRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Client not found with ID: " + clientId));

        clientRepository.delete(existingClient);

        log.info("Client deleted with ID: {}", clientId);
    }

    public ClientResponse getClientById(Long clientId) {
        var client = clientRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Client not found with ID: " + clientId));

        log.info("Retrieved client: {}", client);

        return mapToClientResponse(client);
    }

    private ClientResponse mapToClientResponse(Client client) {
        return ClientResponse.builder()
                .id(client.getId())
                .firstName(client.getFirstName())
                .lastName(client.getLastName())
                .dni(client.getDni())
                .email(client.getEmail())
                .phone(client.getPhone())
                .address(client.getAddress())
                .build();
    }
}
