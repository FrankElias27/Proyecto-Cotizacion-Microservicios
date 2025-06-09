package com.programming.frank.product_microservice.repository;

import com.programming.frank.product_microservice.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

public interface ProductRepository extends JpaRepository <Product,Long> {

    @Query("SELECT p FROM Product p WHERE " +
            "LOWER(p.name) LIKE %:keyword% OR " +
            "LOWER(p.description) LIKE %:keyword% OR " +
            "LOWER(p.skuCode) LIKE %:keyword%")
    Page<Product> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);
}
