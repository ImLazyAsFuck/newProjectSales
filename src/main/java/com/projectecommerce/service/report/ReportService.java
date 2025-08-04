package com.projectecommerce.service.report;

import com.projectecommerce.model.dto.response.ProductStatisticDTO;
import com.projectecommerce.model.dto.response.ReportDTO;

import java.time.LocalDate;
import java.util.List;

public interface ReportService {
    List<ReportDTO> getSalesSummary(String period);
    List<ProductStatisticDTO> getTopSellingProducts(int limit);
    List<ReportDTO> getRevenueBetween(LocalDate start, LocalDate end);
    List<ProductStatisticDTO> getInventoryReport();
}
