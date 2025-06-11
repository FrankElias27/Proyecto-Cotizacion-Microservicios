package com.programming.frank.quotation_microservice.dto;

import com.programming.frank.quotation_microservice.enums.Status;
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

    private Long clientId;
    private String subject;
    private Status status;
    private BigDecimal total;
    private Set<QuotationDetailRequest> details;
    private ClientDetails clientDetails;
}
