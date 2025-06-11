package com.programming.frank.quotation_microservice.services;

import com.programming.frank.quotation_microservice.client.ClientClient;
import com.programming.frank.quotation_microservice.dto.QuotationRequest;
import com.programming.frank.quotation_microservice.dto.QuotationsResponse;
import com.programming.frank.quotation_microservice.enums.Status;
import com.programming.frank.quotation_microservice.event.QuotationEvent;
import com.programming.frank.quotation_microservice.model.Quotation;
import com.programming.frank.quotation_microservice.repository.QuotationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class QuotationService {

    private final QuotationRepository quotationRepository;
    private final ClientClient clientClient;
    private final KafkaTemplate<String, QuotationEvent> kafkaTemplate;

    public void addQuotation(QuotationRequest quotationRequest) {

        var client = clientClient.getClientById(quotationRequest.getClientId());
        if (client == null) {
            throw new RuntimeException("Client with ID " + quotationRequest.getClientId() + " not found.");
        }

        var quotation = Quotation.builder()
                .clientId(quotationRequest.getClientId())
                .subject(quotationRequest.getSubject())
                .total(quotationRequest.getTotal())
                .date(LocalDateTime.now())
                .status(Status.CREATED)
                .build();


        quotationRepository.save(quotation);

        log.info("Quotation saved for client ID {}: {}", quotationRequest.getClientId(), quotation);
    }

    public void updateQuotation(Long quotationId, QuotationRequest quotationRequest) {
        var existingQuotation = quotationRepository.findById(quotationId)
                .orElseThrow(() -> new RuntimeException("Quotation not found with ID: " + quotationId));

        existingQuotation.setDate(LocalDateTime.now());
        existingQuotation.setClientId(quotationRequest.getClientId());
        existingQuotation.setSubject(quotationRequest.getSubject());
        existingQuotation.setStatus(Status.PROCESSED);
        existingQuotation.setTotal(quotationRequest.getTotal());

        quotationRepository.save(existingQuotation);

        //send the message to kafka topics
        QuotationEvent quotationEvent = new QuotationEvent();

        quotationEvent.setQuotationId(quotationId.toString());
        quotationEvent.setSubject(existingQuotation.getSubject());
        quotationEvent.setEmail(quotationRequest.getClientDetails().email());
        log.info("Start sending Quotation Event {} to kafka topic quotation",quotationEvent);
        kafkaTemplate.send("quotation",quotationEvent);
        log.info("End sending Quotation Event {} to kafka topic quotation",quotationEvent);

        log.info("Quotation updated: {}", existingQuotation);
    }


    public QuotationsResponse getQuotationById(Long quotationId) {
        var quotation = quotationRepository.findById(quotationId)
                .orElseThrow(() -> new RuntimeException("Quotation not found with ID: " + quotationId));

        log.info("Retrieved client: {}", quotation);

        return mapToQuotationResponse(quotation);
    }

    public void deleteQuotation(Long quotationId) {
        var existingQuotation = quotationRepository.findById(quotationId)
                .orElseThrow(() -> new RuntimeException("Quotation not found with ID: " + quotationId));

        quotationRepository.delete(existingQuotation);

        log.info("Quotation deleted with ID: {}", quotationId);
    }

    public Page<QuotationsResponse> getQuotationsPage(Pageable pageable) {
        var quotationsPage = quotationRepository.findAll(pageable);
        log.info("Retrieved page {} of quotations, size: {}", quotationsPage.getNumber(), quotationsPage.getSize());

        return quotationsPage.map(this::mapToQuotationResponse);
    }

    private QuotationsResponse mapToQuotationResponse(Quotation quotation) {
        return QuotationsResponse.builder()
                .id(quotation.getId())
                .date(quotation.getDate())
                .client(clientClient.getClientById(quotation.getClientId()))
                .total(quotation.getTotal())
                .subject(quotation.getSubject())
                .status(quotation.getStatus())
                .build();
    }




}
