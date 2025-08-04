package com.projectecommerce.repository;

import com.projectecommerce.model.entity.Invoice;
import com.projectecommerce.model.enums.InvoiceStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface InvoiceRepository extends JpaRepository<Invoice, Integer>{
    List<Invoice> findByStatus(InvoiceStatus status);
    Optional<Invoice> findByOrderId(Long orderId);

    Page<Invoice> findAllByStatus(InvoiceStatus status, Pageable pageable);

    @Query("SELECT i FROM Invoice i WHERE (:status IS NULL OR i.status = :status) AND (:start IS NULL OR i.createdAt >= :start) AND (:end IS NULL OR i.createdAt <= :end)")
    Page<Invoice> filter(@Param("status") InvoiceStatus status,
                         @Param("start") LocalDate start,
                         @Param("end") LocalDate end,
                         Pageable pageable);

    @Query("SELECT FUNCTION('MONTH', i.createdAt) AS month, SUM(i.totalAmount) FROM Invoice i WHERE i.status = 'PAID' GROUP BY FUNCTION('MONTH', i.createdAt)")
    List<Object[]> getMonthlyRevenue();
}
