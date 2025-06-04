package com.programming.frank.product_microservice.repository;

import com.programming.frank.product_microservice.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface ProductRepository extends JpaRepository <Product,Long> {
}
