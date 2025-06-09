package com.programming.frank.product_microservice.service;

import com.programming.frank.product_microservice.dto.ProductRequest;
import com.programming.frank.product_microservice.dto.ProductResponse;
import com.programming.frank.product_microservice.model.Product;
import com.programming.frank.product_microservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
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

        log.info("Retrieved product: {}", product);

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

    public void importProductsFromExcel(MultipartFile file) {
        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            int processedCount = 0;

            for (Row row : sheet) {
                int rowIndex = row.getRowNum();

                // Saltar las primeras dos filas (título general + encabezados)
                if (rowIndex <= 0) {
                    continue;
                }

                // Obtener cada celda por índice (aunque esté vacía)
                Cell idCell = row.getCell(1); // columna B (índice 1)
                Cell nameCell = row.getCell(2); // columna C
                Cell descriptionCell = row.getCell(3); // columna D
                Cell skuCodeCell = row.getCell(4); // columna E
                Cell priceCell = row.getCell(5); // columna F

                Long id = null;
                if (idCell != null) {
                    if (idCell.getCellType() == CellType.NUMERIC) {
                        id = (long) idCell.getNumericCellValue();
                    } else if (idCell.getCellType() == CellType.STRING) {
                        try {
                            id = Long.parseLong(idCell.getStringCellValue().trim());
                        } catch (NumberFormatException ignored) {}
                    }
                }

                String Name = getCellStringValue(nameCell);
                String Description = getCellStringValue(descriptionCell);
                String SkuCode = getCellStringValue(skuCodeCell);
                String Price = getCellStringValue(priceCell);

                if (id != null && productRepository.existsById(id)) {
                    Product existingProduct = productRepository.findById(id).get();
                    existingProduct.setName(Name);
                    existingProduct.setDescription(Description);
                    existingProduct.setSkuCode(SkuCode);
                    existingProduct.setPrice(new BigDecimal(Price.replace(",", ".")));

                    productRepository.save(existingProduct);
                    log.info("Product updated: {}", existingProduct);
                } else {
                    Product newProduct = Product.builder()
                            .name(Name)
                            .description(Description)
                            .skuCode(SkuCode)
                            .price(new BigDecimal(Price.replace(",", ".")))
                            .build();

                    productRepository.save(newProduct);
                    log.info("New product added: {}", newProduct);
                }
                processedCount++;
            }

            log.info("Processed {} rows from Excel file", processedCount);

        } catch (Exception e) {
            log.error("Error processing Excel file", e);
            throw new RuntimeException("Failed to import proucts from Excel file", e);
        }
    }

    private String getCellStringValue(Cell cell) {
        if (cell == null) return null;

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                return String.valueOf(cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            default:
                return null;
        }
    }
}
