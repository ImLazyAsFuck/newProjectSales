package com.projectecommerce.controller;

import com.projectecommerce.model.dto.response.APIResponse;
import com.projectecommerce.model.dto.response.ReportDTO;
import com.projectecommerce.model.dto.response.ProductStatisticDTO;
import com.projectecommerce.service.report.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/sales-summary")
    @PreAuthorize("hasAnyRole('ADMIN','SALES')")
    public ResponseEntity<APIResponse<List<ReportDTO>>> getSalesSummary(
            @RequestParam(defaultValue = "MONTH") String period // WEEK, MONTH, QUARTER, YEAR
    ) {
        List<ReportDTO> data = reportService.getSalesSummary(period.toUpperCase());
        return ResponseEntity.ok(APIResponse.<List<ReportDTO>>builder()
                .data(data)
                .message("Sales summary fetched")
                .success(true)
                .timeStamp(LocalDateTime.now())
                .build());
    }

    @GetMapping("/top-products")
    @PreAuthorize("hasAnyRole('ADMIN','SALES')")
    public ResponseEntity<APIResponse<List<ProductStatisticDTO>>> getTopSellingProducts(
            @RequestParam(defaultValue = "5") int limit
    ) {
        List<ProductStatisticDTO> data = reportService.getTopSellingProducts(limit);
        return ResponseEntity.ok(APIResponse.<List<ProductStatisticDTO>>builder()
                .data(data)
                .message("Top selling products fetched")
                .success(true)
                .timeStamp(LocalDateTime.now())
                .build());
    }

    @GetMapping("/revenue")
    @PreAuthorize("hasAnyRole('ADMIN','SALES')")
    public ResponseEntity<APIResponse<List<ReportDTO>>> getRevenueBetween(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        List<ReportDTO> data = reportService.getRevenueBetween(startDate, endDate);
        return ResponseEntity.ok(APIResponse.<List<ReportDTO>>builder()
                .data(data)
                .message("Revenue report fetched")
                .success(true)
                .timeStamp(LocalDateTime.now())
                .build());
    }

    @GetMapping("/inventory")
    @PreAuthorize("hasAnyRole('ADMIN','SALES')")
    public ResponseEntity<APIResponse<List<ProductStatisticDTO>>> getInventoryReport() {
        List<ProductStatisticDTO> data = reportService.getInventoryReport();
        return ResponseEntity.ok(APIResponse.<List<ProductStatisticDTO>>builder()
                .data(data)
                .message("Inventory report fetched")
                .success(true)
                .timeStamp(LocalDateTime.now())
                .build());
    }
}
