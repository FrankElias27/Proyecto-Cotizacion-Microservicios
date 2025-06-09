package com.programming.frank.client_microservice.service;

import com.programming.frank.client_microservice.dto.ClientRequest;
import com.programming.frank.client_microservice.dto.ClientResponse;
import com.programming.frank.client_microservice.model.Client;
import com.programming.frank.client_microservice.repository.ClientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

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


    public ClientResponse getClientById(Long clientId) {
        var client = clientRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Client not found with ID: " + clientId));

        log.info("Retrieved client: {}", client);

        return mapToClientResponse(client);
    }

    public void deleteClient(Long clientId) {
        var existingClient = clientRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Client not found with ID: " + clientId));

        clientRepository.delete(existingClient);

        log.info("Client deleted with ID: {}", clientId);
    }

    public Page<ClientResponse> getClientsPage(Pageable pageable) {
        var clientsPage = clientRepository.findAll(pageable);
        log.info("Retrieved page {} of clients, size: {}", clientsPage.getNumber(), clientsPage.getSize());

        return clientsPage.map(this::mapToClientResponse);
    }

    public Page<ClientResponse> searchClients(String keyword, Pageable pageable) {
        var clientsPage = clientRepository.searchByKeyword(keyword.toLowerCase(), pageable);

        log.info("Search for keyword '{}', found {} results", keyword, clientsPage.getTotalElements());

        return clientsPage.map(this::mapToClientResponse);
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

    public void importClientsFromExcel(MultipartFile file) {
        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            int processedCount = 0;

            for (Row row : sheet) {
                int rowIndex = row.getRowNum();

                // Saltar las primeras dos filas (título general + encabezados)
                if (rowIndex <= 0) {
                    continue;
                }

                // Obtener cada celda por índice (aunque esté vacía)
                Cell idCell = row.getCell(1); // columna B (índice 1)
                Cell nameCell = row.getCell(2); // columna C
                Cell lastNameCell = row.getCell(3); // columna D
                Cell dniCell = row.getCell(4); // columna E
                Cell emailCell = row.getCell(5); // columna F
                Cell phoneCell = row.getCell(6); // columna G
                Cell addressCell = row.getCell(7); // columna H

                Long id = null;
                if (idCell != null) {
                    if (idCell.getCellType() == CellType.NUMERIC) {
                        id = (long) idCell.getNumericCellValue();
                    } else if (idCell.getCellType() == CellType.STRING) {
                        try {
                            id = Long.parseLong(idCell.getStringCellValue().trim());
                        } catch (NumberFormatException ignored) {}
                    }
                }

                String firstName = getCellStringValue(nameCell);
                String lastName = getCellStringValue(lastNameCell);
                String dni = getCellStringValue(dniCell);
                String email = getCellStringValue(emailCell);
                String phone = getCellStringValue(phoneCell);
                String address = getCellStringValue(addressCell);

                if (id != null && clientRepository.existsById(id)) {
                    Client existingClient = clientRepository.findById(id).get();
                    existingClient.setFirstName(firstName);
                    existingClient.setLastName(lastName);
                    existingClient.setDni(dni);
                    existingClient.setEmail(email);
                    existingClient.setPhone(phone);
                    existingClient.setAddress(address);

                    clientRepository.save(existingClient);
                    log.info("Client updated: {}", existingClient);
                } else {
                    Client newClient = Client.builder()
                            .firstName(firstName)
                            .lastName(lastName)
                            .dni(dni)
                            .email(email)
                            .phone(phone)
                            .address(address)
                            .build();

                    clientRepository.save(newClient);
                    log.info("New client added: {}", newClient);
                }
                processedCount++;
            }

            log.info("Processed {} rows from Excel file", processedCount);

        } catch (Exception e) {
            log.error("Error processing Excel file", e);
            throw new RuntimeException("Failed to import clients from Excel file", e);
        }
    }

    private String getCellStringValue(Cell cell) {
        if (cell == null) return null;

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                return String.valueOf((long) cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            default:
                return null;
        }
    }
}
