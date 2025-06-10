package com.programming.frank.quotation_microservice.repository;

import com.programming.frank.quotation_microservice.model.QuotationDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuotationDetailRepository extends JpaRepository<QuotationDetail,Long> {
    Page<QuotationDetail> findByQuotationId(Long quotationId, Pageable pageable);
}
