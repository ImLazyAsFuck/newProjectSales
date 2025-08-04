package com.projectecommerce.service.order;

import com.projectecommerce.model.entity.Order;
import com.projectecommerce.model.enums.OrderStatus;
import org.springframework.data.domain.Page;

public interface OrderService {
    Page<Order> listOrders(Long userId, boolean isAdmin, int page, int size);
    Order getOrderDetail(Integer orderId, Long userId, boolean isAdmin);
    Order createOrderFromCart(Long userId, String shippingAddress);
    void updateOrderStatus(Integer orderId, OrderStatus status);
    void updateOrderInfo(Integer orderId, Long userId, String address, String notes);
    void cancelOrder(Integer orderId, Long userId, String reason);
}
