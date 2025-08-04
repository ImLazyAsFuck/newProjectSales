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
    @Query("""
        SELECT new com.projectecommerce.model.dto.response.ReportDTO(
            DATE(o.createdAt), 
            SUM(oi.quantity * oi.price)
        )
        FROM OrderItem oi
        JOIN oi.order o
        WHERE o.createdAt BETWEEN :start AND :end
        GROUP BY DATE(o.createdAt)
        ORDER BY DATE(o.createdAt)
        """)
    List<ReportDTO> getRevenueBetween(@Param("start") LocalDate start, @Param("end") LocalDate end);

    @Query("""
        SELECT p.id, p.name, SUM(oi.quantity), SUM(oi.quantity * oi.price), p.stock
        FROM OrderItem oi
        JOIN oi.product p
        GROUP BY p.id, p.name, p.stock
        ORDER BY SUM(oi.quantity) DESC
        """)
    List<Object[]> findTopSellingProducts();

    @Query("""
    SELECT new com.projectecommerce.model.dto.response.ReportDTO(
        CASE 
            WHEN :period = 'daily' THEN FUNCTION('DATE', o.createdAt)
            WHEN :period = 'monthly' THEN FUNCTION('DATE_FORMAT', o.createdAt, '%Y-%m')
            WHEN :period = 'yearly' THEN FUNCTION('YEAR', o.createdAt)
        END,
        SUM(oi.price * oi.quantity)
    )
    FROM OrderItem oi
    JOIN oi.order o
    WHERE o.status = 'COMPLETED'
    GROUP BY 
        CASE 
            WHEN :period = 'daily' THEN FUNCTION('DATE', o.createdAt)
            WHEN :period = 'monthly' THEN FUNCTION('DATE_FORMAT', o.createdAt, '%Y-%m')
            WHEN :period = 'yearly' THEN FUNCTION('YEAR', o.createdAt)
        END
    ORDER BY 1 ASC
""")
    List<ReportDTO> getSalesSummaryByPeriod(@Param("period") String period);

}
