package com.programming.frank.client_microservice.repository;

import com.programming.frank.client_microservice.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client,Long> {
}
