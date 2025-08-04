package com.projectecommerce.mapper;

import com.projectecommerce.model.dto.response.OrderItemResponseDTO;
import com.projectecommerce.model.dto.response.OrderResponseDTO;
import com.projectecommerce.model.dto.response.UserOrder;
import com.projectecommerce.model.entity.Order;

public class OrderMapper{
    public static OrderResponseDTO mapToDTO(Order order) {
        return OrderResponseDTO.builder()
                .id(order.getId())
                .status(order.getStatus())
                .shippingAddress(order.getShippingAddress())
                .internalNotes(order.getInternalNotes())
                .totalPrice(order.getTotalPrice())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .user(UserOrder.builder()
                        .id(order.getUser().getId())
                        .fullName(order.getUser().getFullName())
                        .email(order.getUser().getEmail())
                        .build())
                .orderItems(order.getOrderItems().stream().map(item ->
                        OrderItemResponseDTO.builder()
                                .productId(item.getProduct().getId())
                                .productName(item.getProduct().getName())
                                .quantity(item.getQuantity())
                                .price(item.getPrice())
                                .build()
                ).toList())
                .build();
    }

}
