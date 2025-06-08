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

                if (rowIndex <= 2) {

                    continue;
                }


                Long id = null;
                Cell idCell = row.getCell(0);
                if (idCell != null && idCell.getCellType() == CellType.NUMERIC) {
                    id = (long) idCell.getNumericCellValue();
                } else if (idCell != null && idCell.getCellType() == CellType.STRING) {
                    try {
                        id = Long.parseLong(idCell.getStringCellValue());
                    } catch (NumberFormatException e) {

                        id = null;
                    }
                }

                String firstName = getCellStringValue(row.getCell(1));
                String lastName = getCellStringValue(row.getCell(2));
                String dni = getCellStringValue(row.getCell(3));
                String email = getCellStringValue(row.getCell(4));
                String phone = getCellStringValue(row.getCell(5));
                String address = getCellStringValue(row.getCell(6));

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

        if (cell.getCellType() == CellType.STRING) {
            return cell.getStringCellValue().trim();
        } else if (cell.getCellType() == CellType.NUMERIC) {
            return String.valueOf((long)cell.getNumericCellValue());
        } else if (cell.getCellType() == CellType.BOOLEAN) {
            return String.valueOf(cell.getBooleanCellValue());
        } else {
            return null;
        }
    }
}
