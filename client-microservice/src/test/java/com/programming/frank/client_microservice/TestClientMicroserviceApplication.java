package com.programming.frank.client_microservice;

import org.springframework.boot.SpringApplication;

public class TestClientMicroserviceApplication {

	public static void main(String[] args) {
		SpringApplication.from(ClientMicroserviceApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
