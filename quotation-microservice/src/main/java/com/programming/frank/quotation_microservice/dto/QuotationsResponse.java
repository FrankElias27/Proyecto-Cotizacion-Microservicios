package com.programming.frank.quotation_microservice.dto;

import com.programming.frank.quotation_microservice.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QuotationsResponse {

    private Long id;
    private LocalDateTime date;
    private Long clientId;
    private String clientName;
    private String subject;
    private Status status;
    private BigDecimal total;
    private Set<QuotationDetailResponse> details;
}
