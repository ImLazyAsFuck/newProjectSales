package com.projectecommerce.model.dto.response;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartResponseDTO {
    private Long id;
    private Long productId;
    private String productName;
    private int quantity;
    private Double price;
}
