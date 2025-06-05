package com.programming.frank.quotation_microservice.controller;

import com.programming.frank.quotation_microservice.dto.QuotationRequest;
import com.programming.frank.quotation_microservice.services.QuotationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/quotation")
@RequiredArgsConstructor
public class QuotationController {

    private final QuotationService quotationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void addQuotation(@RequestBody QuotationRequest quotationRequest) {
        this.quotationService.addQuotation(quotationRequest);
    }
}
