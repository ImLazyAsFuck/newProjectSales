package com.projectecommerce.model.dto.response;

import com.projectecommerce.model.enums.OrderStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderResponseDTO {
    private Integer id;
    private UserOrder user;
    private OrderStatus status;
    private String shippingAddress;
    private String internalNotes;
    private BigDecimal totalPrice;
    private LocalDate createdAt;
    private LocalDate updatedAt;
    private List<OrderItemResponseDTO> orderItems;
}
