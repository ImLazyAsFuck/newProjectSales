package com.projectecommerce.model.dto.response;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class OrderDTO {
    private Integer id;
    private LocalDateTime createdAt;
    private String status;
    private BigDecimal totalAmount;
    private String shippingAddress;
    private String customerName;
}
