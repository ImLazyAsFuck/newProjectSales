package com.projectecommerce.repository;

import com.projectecommerce.model.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Integer>{
    Page<Order> findByUserId(Long userId, Pageable pageable);
}
