package com.projectecommerce.model.dto.response;

import lombok.*;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderItemResponseDTO {
    private Long productId;
    private String productName;
    private Integer quantity;
    private BigDecimal price;
}
