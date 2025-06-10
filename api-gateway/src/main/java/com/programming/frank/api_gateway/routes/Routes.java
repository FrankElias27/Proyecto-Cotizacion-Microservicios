package com.programming.frank.api_gateway.routes;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.server.mvc.filter.CircuitBreakerFilterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerAdapter;
import org.springframework.web.servlet.function.HandlerFunction;
import org.springframework.web.servlet.function.RequestPredicates;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import java.net.URI;

import static org.springframework.cloud.gateway.server.mvc.filter.FilterFunctions.setPath;
import static org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions.route;

@Configuration
public class Routes {

    @Value("${product.service.url}")
    private String productServiceUrl;
    @Value("${client.service.url}")
    private String clientServiceUrl;
    @Value("${quotation.service.url}")
    private String quotationServiceUrl;

    @Bean
    public RouterFunction<ServerResponse> productServiceRoute(){
        return GatewayRouterFunctions.route("product_service")
                .route(RequestPredicates.path("/api/product/**"), HandlerFunctions.http(productServiceUrl))
                .filter(CircuitBreakerFilterFunctions.circuitBreaker("productServiceCircuitBreaker",
                        URI.create("forward:/fallbackRoute")))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> productServiceSwaggerRoute(){
        return GatewayRouterFunctions.route("product_service_swagger")
                .route(RequestPredicates.path("/aggregate/product-service/v3/api-docs"), HandlerFunctions.http(productServiceUrl))
                .filter(CircuitBreakerFilterFunctions.circuitBreaker("productSwaggerCircuitBreaker",
                        URI.create("forward:/fallbackRoute")))
                .filter(setPath("/api-docs"))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> clientServiceRoute(){
        return GatewayRouterFunctions.route("client_service")
                .route(RequestPredicates.path("/api/client/**"), HandlerFunctions.http(clientServiceUrl))
                .filter(CircuitBreakerFilterFunctions.circuitBreaker("clientServiceCircuitBreaker",
                        URI.create("forward:/fallbackRoute")))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> clientServiceSwaggerRoute(){
        return GatewayRouterFunctions.route("client_service_swagger")
                .route(RequestPredicates.path("/aggregate/client-service/v3/api-docs"), HandlerFunctions.http(clientServiceUrl))
                .filter(CircuitBreakerFilterFunctions.circuitBreaker("clientSwaggerCircuitBreaker",
                        URI.create("forward:/fallbackRoute")))
                .filter(setPath("/api-docs"))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> quotationServiceRoute(){
        return GatewayRouterFunctions.route("quotation_service")
                .route(RequestPredicates.path("/api/quotation/**"), HandlerFunctions.http(quotationServiceUrl))
                .filter(CircuitBreakerFilterFunctions.circuitBreaker("quotationServiceCircuitBreaker",
                        URI.create("forward:/fallbackRoute")))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> quotationServiceSwaggerRoute(){
        return GatewayRouterFunctions.route("quotation_service_swagger")
                .route(RequestPredicates.path("/aggregate/quotation-service/v3/api-docs"), HandlerFunctions.http(quotationServiceUrl))
                .filter(CircuitBreakerFilterFunctions.circuitBreaker("quotationSwaggerCircuitBreaker",
                        URI.create("forward:/fallbackRoute")))
                .filter(setPath("/api-docs"))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> quotationDetailServiceRoute(){
        return GatewayRouterFunctions.route("quotation_service")
                .route(RequestPredicates.path("/api/quotation-detail/**"), HandlerFunctions.http(quotationServiceUrl))
                .filter(CircuitBreakerFilterFunctions.circuitBreaker("quotationServiceCircuitBreaker",
                        URI.create("forward:/fallbackRoute")))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> fallbackRoute() {
        return route("fallbackRoute")
                .GET("/fallbackRoute", request -> ServerResponse.status(HttpStatus.SERVICE_UNAVAILABLE)
                        .body("Service Unavailable, please try again later"))
                .build();
    }

}
