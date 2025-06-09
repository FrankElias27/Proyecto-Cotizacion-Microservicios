package com.programming.frank.product_microservice.controller;

import com.programming.frank.product_microservice.dto.ProductRequest;
import com.programming.frank.product_microservice.dto.ProductResponse;
import com.programming.frank.product_microservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void addProduct(@RequestBody ProductRequest productRequest) {
        this.productService.addProduct(productRequest);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ProductResponse> getAllProducts() {
        return this.productService.getAllProducts();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ProductResponse getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateProduct(@PathVariable Long id, @RequestBody ProductRequest productRequest) {
        this.productService.updateProduct(id, productRequest);
    }

    @GetMapping("/page")
    @ResponseStatus(HttpStatus.OK)
    public Page<ProductResponse> getProducts(Pageable pageable) {
        return productService.getProductsPage(pageable);
    }

    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public Page<ProductResponse> searchProducts(
            @RequestParam("query") String query,
            Pageable pageable
    ) {
        return productService.searchProducts(query, pageable);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable Long id) {
        this.productService.deleteProduct(id);
    }
}
