package com.programming.frank.product_microservice.service;

import com.programming.frank.product_microservice.dto.ProductRequest;
import com.programming.frank.product_microservice.dto.ProductResponse;
import com.programming.frank.product_microservice.model.Product;
import com.programming.frank.product_microservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;

    public void addProduct(ProductRequest productRequest) {
        var product = Product.builder()
                .name(productRequest.getName())
                .description(productRequest.getDescription())
                .skuCode(productRequest.getSkuCode())
                .price(productRequest.getPrice())
                .build();

        productRepository.save(product);

        log.info("Product added: {}", product);
    }

    public List<ProductResponse> getAllProducts() {
        var products = productRepository.findAll();

        log.info("Retrieved {} products", products.size());

        return products.stream().map(this::mapToProductResponse).toList();
    }


    public void updateProduct(Long productId, ProductRequest productRequest) {
        var existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + productId));

        existingProduct.setName(productRequest.getName());
        existingProduct.setDescription(productRequest.getDescription());
        existingProduct.setSkuCode(productRequest.getSkuCode());
        existingProduct.setPrice(productRequest.getPrice());

        productRepository.save(existingProduct);

        log.info("Product updated: {}", existingProduct);
    }

    public ProductResponse getProductById(Long productId) {
        var product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + productId));

        log.info("Retrieved client: {}", product);

        return mapToProductResponse(product);
    }

    public void deleteProduct(Long productId) {
        var existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found with ID: " + productId));

        productRepository.delete(existingProduct);

        log.info("Product deleted with ID: {}", productId);
    }

    public Page<ProductResponse> getProductsPage(Pageable pageable) {
        var productsPage = productRepository.findAll(pageable);
        log.info("Retrieved page {} of products, size: {}", productsPage.getNumber(), productsPage.getSize());

        return productsPage.map(this::mapToProductResponse);
    }

    public Page<ProductResponse> searchProducts(String keyword, Pageable pageable) {
        var productsPage = productRepository.searchByKeyword(keyword.toLowerCase(), pageable);

        log.info("Search for keyword '{}', found {} results", keyword, productsPage.getTotalElements());

        return productsPage.map(this::mapToProductResponse);
    }

    private ProductResponse mapToProductResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .skuCode(product.getSkuCode())
                .price(product.getPrice())
                .build();
    }
}
