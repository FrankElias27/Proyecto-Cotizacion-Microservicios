package com.programming.frank.quotation_microservice.config;

import com.programming.frank.quotation_microservice.client.ClientClient;
import io.micrometer.observation.ObservationRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
@RequiredArgsConstructor
public class RestClientConfig {

    @Value("${client.service.url}")
    private String clientServiceUrl;

    private final ObservationRegistry observationRegistry;

    @Bean
    public ClientClient clientClient(){

        RestClient restClient = RestClient.builder()
                .baseUrl(clientServiceUrl)
                .observationRegistry(observationRegistry)
                .build();
        var restClientAdapter = RestClientAdapter.create(restClient);
        var httpServiceProxyFactory = HttpServiceProxyFactory.builderFor(restClientAdapter).build();
        return httpServiceProxyFactory.createClient(ClientClient.class);

    }
}
