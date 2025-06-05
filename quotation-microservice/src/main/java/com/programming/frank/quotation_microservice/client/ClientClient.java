package com.programming.frank.quotation_microservice.client;

import com.programming.frank.quotation_microservice.dto.ClientResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;


public interface ClientClient {

    @GetExchange("/api/client/{id}")
    ClientResponse getClientById(@PathVariable("id") Long id);

}
