package com.projectecommerce.service.order;

import com.projectecommerce.model.entity.CartItem;
import com.projectecommerce.model.entity.Order;
import com.projectecommerce.model.entity.OrderItem;
import com.projectecommerce.model.entity.Product;
import com.projectecommerce.model.enums.OrderStatus;
import com.projectecommerce.repository.*;
import com.projectecommerce.utils.exception.ConflictException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderItemRepository orderItemRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Override
    public Page<Order> listOrders(Long userId, boolean isAdmin, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return isAdmin ? orderRepository.findAll(pageable)
                       : orderRepository.findByUserId(userId, pageable);
    }

    @Override
    public Order getOrderDetail(Integer orderId, Long userId, boolean isAdmin) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));
        if (!isAdmin && !order.getUser().getId().equals(userId)) {
            throw new ConflictException("Not allowed to view this order");
        }
        return order;
    }

    @Override
    public Order createOrderFromCart(Long userId, String shippingAddress) {
        List<CartItem> cartItems = cartItemRepository.findByUserId(userId);
        if (cartItems.isEmpty()) throw new IllegalStateException("Cart is empty");

        BigDecimal total = BigDecimal.ZERO;
        List<OrderItem> orderItems = new ArrayList<>();

        for (CartItem item : cartItems) {
            Product product = item.getProduct();
            if (product.getStock() < item.getQuantity()) {
                throw new IllegalStateException("Insufficient stock for product " + product.getName());
            }
            product.setStock(product.getStock() - item.getQuantity());
            productRepository.save(product);

            BigDecimal itemTotal = BigDecimal.valueOf(product.getPrice()).multiply(BigDecimal.valueOf(item.getQuantity()));
            total = total.add(itemTotal);

            orderItems.add(OrderItem.builder()
                    .product(product)
                    .quantity(item.getQuantity())
                    .price(BigDecimal.valueOf(product.getPrice()))
                    .build());
        }

        Order order = Order.builder()
                .user(userRepository.getReferenceById(userId))
                .status(OrderStatus.PENDING)
                .shippingAddress(shippingAddress)
                .totalPrice(total)
                .orderItems(orderItems)
                .build();

        orderItems.forEach(item -> item.setOrder(order));

        cartItemRepository.deleteByUserId(userId);
        return orderRepository.save(order);
    }

    @Override
    public void updateOrderStatus(Integer orderId, OrderStatus status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));
        if (order.getStatus() == OrderStatus.SHIPPED) {
            throw new IllegalStateException("Cannot update a delivered order.");
        }
        order.setStatus(status);
        orderRepository.save(order);
    }
}
