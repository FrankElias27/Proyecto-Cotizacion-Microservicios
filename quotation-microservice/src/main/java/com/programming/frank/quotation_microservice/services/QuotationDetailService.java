package com.programming.frank.quotation_microservice.services;

import com.programming.frank.quotation_microservice.client.ClientClient;
import com.programming.frank.quotation_microservice.client.ProductClient;
import com.programming.frank.quotation_microservice.dto.QuotationDetailRequest;
import com.programming.frank.quotation_microservice.dto.QuotationDetailResponse;
import com.programming.frank.quotation_microservice.dto.QuotationRequest;
import com.programming.frank.quotation_microservice.dto.QuotationsResponse;
import com.programming.frank.quotation_microservice.model.Quotation;
import com.programming.frank.quotation_microservice.model.QuotationDetail;
import com.programming.frank.quotation_microservice.repository.QuotationDetailRepository;
import com.programming.frank.quotation_microservice.repository.QuotationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class QuotationDetailService {

    private final QuotationDetailRepository quotationDetailRepository;
    private final QuotationRepository quotationRepository;
    private final ProductClient productClient;

    public void addQuotationDetail(QuotationDetailRequest quotationDetailRequest) {

        var product = productClient.getProductById(quotationDetailRequest.getProductId());
        if (product == null) {
            throw new RuntimeException("Product with ID " + quotationDetailRequest.getProductId() + " not found.");
        }

        Quotation quotation = quotationRepository.findById(quotationDetailRequest.getQuotationId())
                .orElseThrow(() -> new RuntimeException("Quotation not found"));

        var quotationDetail = QuotationDetail.builder()
                .quotation(quotation)
                .productId(quotationDetailRequest.getProductId())
                .quantity(quotationDetailRequest.getQuantity())
                .unitPrice(quotationDetailRequest.getUnitPrice())
                .subtotal(
                        quotationDetailRequest.getUnitPrice()
                                .multiply(BigDecimal.valueOf(quotationDetailRequest.getQuantity()))
                )
                .build();


        quotationDetailRepository.save(quotationDetail);

        log.info("Quotation Detail saved for product ID {}: {}", quotationDetailRequest.getProductId(), quotationDetail);
    }

    public Page<QuotationDetailResponse> getQuotationDetailsPage(Pageable pageable) {
        var quotationsDetailPage = quotationDetailRepository.findAll(pageable);
        log.info("Retrieved page {} of quotationDetails, size: {}", quotationsDetailPage.getNumber(), quotationsDetailPage.getSize());

        return quotationsDetailPage.map(this::mapToQuotationDetailResponse);
    }

    private QuotationDetailResponse mapToQuotationDetailResponse(QuotationDetail quotation) {
        return QuotationDetailResponse.builder()
                .id(quotation.getId())
                .quotationId(quotation.getQuotation().getId())
                .product(productClient.getProductById(quotation.getProductId()))
                .quantity(quotation.getQuantity())
                .unitPrice(quotation.getUnitPrice())
                .subtotal(quotation.getSubtotal())
                .build();
    }
}
