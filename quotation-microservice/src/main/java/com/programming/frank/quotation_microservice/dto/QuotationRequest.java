package com.programming.frank.quotation_microservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuotationRequest {

    Long clientId;
    String subject;
    BigDecimal total;
    Set<QuotationDetailRequest> details;

}
