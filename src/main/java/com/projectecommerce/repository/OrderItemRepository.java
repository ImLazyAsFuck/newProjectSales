package com.projectecommerce.repository;

import com.projectecommerce.model.dto.response.ReportDTO;
import com.projectecommerce.model.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Integer>{
    boolean existsByProductId(Long productId);
}