package com.projectecommerce.service.report;

import com.projectecommerce.model.dto.response.ProductStatisticDTO;
import com.projectecommerce.model.dto.response.ReportDTO;
import com.projectecommerce.repository.OrderItemRepository;
import com.projectecommerce.repository.ProductRepository;
import com.projectecommerce.service.report.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportServiceImpl implements ReportService {

    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;

    @Override
    public List<ReportDTO> getSalesSummary(String period) {
        return orderItemRepository.getSalesSummaryByPeriod(period);
    }

    @Override
    public List<ProductStatisticDTO> getTopSellingProducts(int limit) {
        return orderItemRepository.findTopSellingProducts()
                .stream()
                .map(row -> ProductStatisticDTO.builder()
                        .productId((Long) row[0])
                        .productName((String) row[1])
                        .totalSold(((Number) row[2]).intValue())
                        .totalRevenue(((Number) row[3]).doubleValue())
                        .stock(((Number) row[4]).intValue())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public List<ReportDTO> getRevenueBetween(LocalDate start, LocalDate end) {
        return orderItemRepository.getRevenueBetween(start, end);
    }

    @Override
    public List<ProductStatisticDTO> getInventoryReport() {
        return productRepository.findAll().stream()
                .map(p -> ProductStatisticDTO.builder()
                        .productId(p.getId())
                        .productName(p.getName())
                        .totalSold(0)
                        .totalRevenue(0.0)
                        .stock(p.getStock())
                        .build())
                .collect(Collectors.toList());
    }
}
