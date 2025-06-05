package com.programming.frank.quotation_microservice.dto;

import com.programming.frank.quotation_microservice.model.Quotation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuotationDetailRequest {

    Long quotationId;
    Long productId;
    Integer quantity;
    BigDecimal unitPrice;
}
