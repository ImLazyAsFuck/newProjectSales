package com.projectecommerce.mapper;

import com.projectecommerce.model.dto.response.OrderItemResponseDTO;
import com.projectecommerce.model.dto.response.OrderResponseDTO;
import com.projectecommerce.model.entity.Order;
import com.projectecommerce.model.entity.OrderItem;

import java.util.List;
import java.util.stream.Collectors;

public class OrderMapper {

    public static OrderResponseDTO mapToDTO(Order order) {
        return OrderResponseDTO.builder()
                .id(order.getId())
                .status(order.getStatus())
                .shippingAddress(order.getShippingAddress())
                .totalPrice(order.getTotalPrice())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .orderItems(order.getOrderItems() != null
                        ? mapOrderItemsToDTO(order.getOrderItems())
                        : null)
                .build();
    }

    public static List<OrderItemResponseDTO> mapOrderItemsToDTO(List<OrderItem> items) {
        return items.stream().map(item -> OrderItemResponseDTO.builder()
                        .id(item.getId())
                        .productId(item.getProduct().getId())
                        .productName(item.getProduct().getName())
                        .quantity(item.getQuantity())
                        .price(item.getPrice())
                        .build())
                .collect(Collectors.toList());
    }

    public static List<OrderResponseDTO> mapToDTOList(List<Order> orders) {
        return orders.stream().map(OrderMapper::mapToDTO).collect(Collectors.toList());
    }
}
