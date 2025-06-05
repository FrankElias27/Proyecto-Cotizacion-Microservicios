package com.programming.frank.quotation_microservice;

import org.springframework.boot.SpringApplication;

public class TestQuotationMicroserviceApplication {

	public static void main(String[] args) {
		SpringApplication.from(QuotationMicroserviceApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
