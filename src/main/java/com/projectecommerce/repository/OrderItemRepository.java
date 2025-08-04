package com.projectecommerce.repository;

import com.projectecommerce.model.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Integer>{
    boolean existsByProductId(Long productId);
}
