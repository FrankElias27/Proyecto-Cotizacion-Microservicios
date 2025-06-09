package com.programming.frank.quotation_microservice.controller;

import com.programming.frank.quotation_microservice.dto.QuotationDetailRequest;
import com.programming.frank.quotation_microservice.dto.QuotationDetailResponse;
import com.programming.frank.quotation_microservice.dto.QuotationRequest;
import com.programming.frank.quotation_microservice.dto.QuotationsResponse;
import com.programming.frank.quotation_microservice.services.QuotationDetailService;
import com.programming.frank.quotation_microservice.services.QuotationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/quotation-detail")
@RequiredArgsConstructor
public class QuotationDetailController {

    private final QuotationDetailService quotationDetailService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void addQuotationDetail(@RequestBody QuotationDetailRequest quotationDetailRequest) {
        this.quotationDetailService.addQuotationDetail(quotationDetailRequest);
    }

    @GetMapping("/page")
    @ResponseStatus(HttpStatus.OK)
    public Page<QuotationDetailResponse> getQuotationDetails(Pageable pageable) {
        return quotationDetailService.getQuotationDetailsPage(pageable);
    }
}
