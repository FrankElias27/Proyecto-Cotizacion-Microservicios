package com.programming.frank.client_microservice.repository;

import com.programming.frank.client_microservice.model.Client;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ClientRepository extends JpaRepository<Client,Long> {

    @Query("SELECT c FROM Client c WHERE " +
            "LOWER(c.firstName) LIKE %:keyword% OR " +
            "LOWER(c.lastName) LIKE %:keyword% OR " +
            "LOWER(c.dni) LIKE %:keyword% OR " +
            "LOWER(c.email) LIKE %:keyword% OR " +
            "LOWER(c.phone) LIKE %:keyword%")
    Page<Client> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);
}
