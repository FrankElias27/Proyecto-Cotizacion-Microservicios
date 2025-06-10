package com.programming.frank.quotation_microservice.controller;

import com.programming.frank.quotation_microservice.dto.QuotationRequest;
import com.programming.frank.quotation_microservice.dto.QuotationsResponse;
import com.programming.frank.quotation_microservice.dto.Report;
import com.programming.frank.quotation_microservice.enums.TypeReport;
import com.programming.frank.quotation_microservice.services.QuotationService;
import com.programming.frank.quotation_microservice.services.ReportService;
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

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

@RestController
@RequestMapping("/api/quotation")
@RequiredArgsConstructor
public class QuotationController {

    private final QuotationService quotationService;
    private final ReportService reportService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void addQuotation(@RequestBody QuotationRequest quotationRequest) {
        this.quotationService.addQuotation(quotationRequest);
    }

    @GetMapping("/page")
    @ResponseStatus(HttpStatus.OK)
    public Page<QuotationsResponse> getQuotations(Pageable pageable) {
        return quotationService.getQuotationsPage(pageable);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public QuotationsResponse getQuotationById(@PathVariable Long id) {
        return quotationService.getQuotationById(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateQuotation(@PathVariable Long id, @RequestBody QuotationRequest quotationRequest) {
        this.quotationService.updateQuotation(id, quotationRequest);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteQuotation(@PathVariable Long id) {
        this.quotationService.deleteQuotation(id);
    }

    @GetMapping(path = "/report")
    public ResponseEntity<Resource> download(@RequestParam Map<String, Object> params)
            throws JRException, IOException, SQLException {
        Report dto = reportService.getReportQuotation(params);

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


}
