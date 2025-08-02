package com.projectecommerce.repository;

import com.projectecommerce.model.entity.Invoice;
import com.projectecommerce.model.enums.InvoiceStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InvoiceRepository extends JpaRepository<Invoice, Integer>{
    List<Invoice> findByStatus(InvoiceStatus status);
}
