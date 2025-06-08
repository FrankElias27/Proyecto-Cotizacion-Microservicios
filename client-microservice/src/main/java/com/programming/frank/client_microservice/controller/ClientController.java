package com.programming.frank.client_microservice.controller;

import com.programming.frank.client_microservice.dto.ClientRequest;
import com.programming.frank.client_microservice.dto.ClientResponse;
import com.programming.frank.client_microservice.dto.Report;
import com.programming.frank.client_microservice.enums.TypeReport;
import com.programming.frank.client_microservice.service.ClientService;
import com.programming.frank.client_microservice.service.ReportService;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.core.io.InputStreamResource;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;



import net.sf.jasperreports.engine.JRException;


import java.util.List;

@RestController
@RequestMapping("/api/client")
@RequiredArgsConstructor
public class ClientController {

    private final ClientService clientService;

    private final ReportService reportService;

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

    @GetMapping("/page")
    @ResponseStatus(HttpStatus.OK)
    public Page<ClientResponse> getClients(Pageable pageable) {
        return clientService.getClientsPage(pageable);
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public Page<ClientResponse> searchClients(
            @RequestParam("query") String query,
            Pageable pageable
    ) {
        return clientService.searchClients(query, pageable);
    }



    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteClient(@PathVariable Long id) {
        this.clientService.deleteClient(id);
    }

    @GetMapping(path = "/report")
    public ResponseEntity<Resource> download(@RequestParam Map<String, Object> params)
            throws JRException, IOException, SQLException {
        Report dto = reportService.getReportClient(params);

        InputStreamResource streamResource = new InputStreamResource(dto.getStream());
        MediaType mediaType = null;
        if (params.get("tipo").toString().equalsIgnoreCase(TypeReport.EXCEL.name())) {
            mediaType = MediaType.APPLICATION_OCTET_STREAM;
        } else {
            mediaType = MediaType.APPLICATION_PDF;
        }

        return ResponseEntity.ok().header("Content-Disposition", "inline; filename=\"" + dto.getFileName() + "\"")
                .contentLength(dto.getLength()).contentType(mediaType).body(streamResource);
    }

    @PostMapping("/import")
    public ResponseEntity<String> importClients(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("El archivo está vacío");
        }

        if (!file.getOriginalFilename().endsWith(".xlsx") && !file.getOriginalFilename().endsWith(".xls")) {
            return ResponseEntity.badRequest().body("Formato de archivo no soportado. Debe ser .xls o .xlsx");
        }

        try {
            clientService.importClientsFromExcel(file);
            return ResponseEntity.ok("Archivo importado exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al importar archivo: " + e.getMessage());
        }
    }
}
