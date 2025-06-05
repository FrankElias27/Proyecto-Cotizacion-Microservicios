package com.programming.frank.quotation_microservice.repository;

import com.programming.frank.quotation_microservice.model.Quotation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuotationRepository extends JpaRepository<Quotation,Long> {
}
