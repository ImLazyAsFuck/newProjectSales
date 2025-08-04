package com.projectecommerce.repository;

import com.projectecommerce.model.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Integer>{
    Optional<Payment> findByTransactionId(String transactionId);
    List<Payment> findAllByInvoiceId(Integer invoiceId);
}