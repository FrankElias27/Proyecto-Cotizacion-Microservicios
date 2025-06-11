package com.programming.frank.notification_microservice;

import org.springframework.boot.SpringApplication;

public class TestNotificationMicroserviceApplication {

	public static void main(String[] args) {
		SpringApplication.from(NotificationMicroserviceApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
