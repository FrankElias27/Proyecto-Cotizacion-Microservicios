package com.programming.frank.quotation_microservice.repository;

import com.programming.frank.quotation_microservice.model.QuotationDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuotationDetailRepository extends JpaRepository<QuotationDetail,Long> {
}
