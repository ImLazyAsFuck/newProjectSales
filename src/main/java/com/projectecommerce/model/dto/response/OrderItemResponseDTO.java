package com.projectecommerce.model.dto.response;

import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemResponseDTO {
    private Integer id;
    private Long productId;
    private String productName;
    private Integer quantity;
    private BigDecimal price;
}
