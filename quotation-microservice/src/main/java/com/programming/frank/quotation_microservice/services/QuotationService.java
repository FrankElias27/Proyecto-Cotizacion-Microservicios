package com.programming.frank.quotation_microservice.services;

import com.programming.frank.quotation_microservice.client.ClientClient;
import com.programming.frank.quotation_microservice.dto.QuotationRequest;
import com.programming.frank.quotation_microservice.model.Quotation;
import com.programming.frank.quotation_microservice.repository.QuotationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class QuotationService {

    private final QuotationRepository quotationRepository;
    private final ClientClient clientClient;

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
                .build();


        quotationRepository.save(quotation);

        log.info("Quotation saved for client ID {}: {}", quotationRequest.getClientId(), quotation);
    }
}
