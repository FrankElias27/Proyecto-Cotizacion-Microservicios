package com.programming.frank.quotation_microservice.config;

import com.programming.frank.quotation_microservice.client.ClientClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class RestClientConfig {

    @Value("${client.service.url}")
    private String clientServiceUrl;

    @Bean
    public ClientClient clientClient(){

        RestClient restClient = RestClient.builder()
                .baseUrl(clientServiceUrl)
                .build();
        var restClientAdapter = RestClientAdapter.create(restClient);
        var httpServiceProxyFactory = HttpServiceProxyFactory.builderFor(restClientAdapter).build();
        return httpServiceProxyFactory.createClient(ClientClient.class);

    }
}
