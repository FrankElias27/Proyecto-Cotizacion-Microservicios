package com.programming.frank.product_microservice.controller;

import com.programming.frank.product_microservice.dto.ProductRequest;
import com.programming.frank.product_microservice.dto.ProductResponse;
import com.programming.frank.product_microservice.dto.Report;
import com.programming.frank.product_microservice.enums.TypeReport;
import com.programming.frank.product_microservice.service.ProductService;
import com.programming.frank.product_microservice.service.ReportService;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.JRException;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final ReportService reportService;

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

    @GetMapping(path = "/report")
    public ResponseEntity<Resource> download(@RequestParam Map<String, Object> params)
            throws JRException, IOException, SQLException {
        Report dto = reportService.getReportProduct(params);

        InputStreamResource streamResource = new InputStreamResource(dto.getStream());
        MediaType mediaType = null;
        if (params.get("tipo").toString().equalsIgnoreCase(TypeReport.EXCEL.name())) {
            mediaType = MediaType.APPLICATION_OCTET_STREAM;
        } else {
            mediaType = MediaType.APPLICATION_PDF;
        }

        return ResponseEntity.ok().header("Content-Disposition", "inline; filename=\"" + dto.getFileName() + "\"")
                .contentLength(dto.getLength()).contentType(mediaType).body(streamResource);
    }

    @PostMapping("/import")
    public ResponseEntity<String> importProducts(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("El archivo está vacío");
        }

        if (!file.getOriginalFilename().endsWith(".xlsx") && !file.getOriginalFilename().endsWith(".xls")) {
            return ResponseEntity.badRequest().body("Formato de archivo no soportado. Debe ser .xls o .xlsx");
        }

        try {
            productService.importProductsFromExcel(file);
            return ResponseEntity.ok("Archivo importado exitosamente");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al importar archivo: " + e.getMessage());
        }
    }
}
