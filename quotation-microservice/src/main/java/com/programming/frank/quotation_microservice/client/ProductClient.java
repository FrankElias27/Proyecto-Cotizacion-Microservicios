package com.programming.frank.quotation_microservice.client;

import com.programming.frank.quotation_microservice.dto.ProductResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;

public interface ProductClient {

    @GetExchange("/api/product/{id}")
    ProductResponse getProductById(@PathVariable("id") Long id);

}
