package com.projectecommerce.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductStatisticDTO {
    private Long productId;
    private String productName;
    private int totalSold;
    private double totalRevenue;
    private int stock;
}
