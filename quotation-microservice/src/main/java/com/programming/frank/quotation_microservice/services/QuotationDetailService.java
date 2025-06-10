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
import java.util.List;

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
                .productName(product.getName())
                .quantity(quotationDetailRequest.getQuantity())
                .unitPrice(product.getPrice())
                .subtotal(
                        product.getPrice()
                                .multiply(BigDecimal.valueOf(quotationDetailRequest.getQuantity()))
                )
                .build();


        quotationDetailRepository.save(quotationDetail);

        log.info("Quotation Detail saved for product ID {}: {}", quotationDetailRequest.getProductId(), quotationDetail);
    }

    public Page<QuotationDetailResponse> getPagedDetailsByQuotationId(Long quotationId, Pageable pageable) {
        log.info("Fetching paged details for quotation ID: {}, page: {}, size: {}",
                quotationId, pageable.getPageNumber(), pageable.getPageSize());

        Page<QuotationDetail> detailPage = quotationDetailRepository.findByQuotationId(quotationId, pageable);

        if (detailPage.isEmpty()) {
            log.warn("No details found for quotation ID: {}", quotationId);
        } else {
            log.info("Retrieved {} details for quotation ID: {}", detailPage.getTotalElements(), quotationId);
        }

        return detailPage.map(this::mapToQuotationDetailResponse);
    }

    public List<QuotationDetailResponse> getAllDetailsByQuotationId(Long quotationId) {
        log.info("Fetching all details for quotation ID: {}", quotationId);

        var details = quotationDetailRepository.findByQuotationId(quotationId);

        if (details.isEmpty()) {
            log.warn("No details found for quotation ID: {}", quotationId);
        } else {
            log.info("Retrieved {} details for quotation ID: {}", details.size(), quotationId);
        }

        return details.stream()
                .map(this::mapToQuotationDetailResponse)
                .toList();
    }

    public void deleteQuotationDetail(Long quotationDetailId) {
        var existingQuotationDetail = quotationDetailRepository.findById(quotationDetailId)
                .orElseThrow(() -> new RuntimeException("Quotation Detail not found with ID: " + quotationDetailId));

        quotationDetailRepository.delete(existingQuotationDetail);

        log.info("Quotation Detail deleted with ID: {}", quotationDetailId);
    }


    private QuotationDetailResponse mapToQuotationDetailResponse(QuotationDetail quotation) {
        return QuotationDetailResponse.builder()
                .id(quotation.getId())
                .quotationId(quotation.getQuotation().getId())
                .product(productClient.getProductById(quotation.getProductId()))
                .productName(quotation.getProductName())
                .quantity(quotation.getQuantity())
                .unitPrice(quotation.getUnitPrice())
                .subtotal(quotation.getSubtotal())
                .build();
    }
}
