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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/quotation-detail")
@RequiredArgsConstructor
public class QuotationDetailController {

    private final QuotationDetailService quotationDetailService;

    @GetMapping("/{quotationId}")
    @ResponseStatus(HttpStatus.OK)
    public List<QuotationDetailResponse> getAllDetailsByQuotationId(@PathVariable Long quotationId) {
        return quotationDetailService.getAllDetailsByQuotationId(quotationId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void addQuotationDetail(@RequestBody QuotationDetailRequest quotationDetailRequest) {
        this.quotationDetailService.addQuotationDetail(quotationDetailRequest);
    }

    @GetMapping("/page/quotation/{quotationId}")
    @ResponseStatus(HttpStatus.OK)
    public Page<QuotationDetailResponse> getQuotationDetails(@PathVariable("quotationId") Long quotationId, Pageable pageable) {
        return quotationDetailService.getPagedDetailsByQuotationId(quotationId, pageable);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteQuotationDetail(@PathVariable Long id) {
        this.quotationDetailService.deleteQuotationDetail(id);
    }


}
