package com.programming.frank.client_microservice.controller;

import com.programming.frank.client_microservice.dto.ClientRequest;
import com.programming.frank.client_microservice.dto.ClientResponse;
import com.programming.frank.client_microservice.service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/client")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void addClient(@RequestBody ClientRequest clientRequest) {
        this.clientService.addClient(clientRequest);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ClientResponse> getAllClients() {
        return this.clientService.getAllClients();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ClientResponse getClientById(@PathVariable Long id) {
        return clientService.getClientById(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateClient(@PathVariable Long id, @RequestBody ClientRequest clientRequest) {
        this.clientService.updateClient(id, clientRequest);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteClient(@PathVariable Long id) {
        this.clientService.deleteClient(id);
    }
}
