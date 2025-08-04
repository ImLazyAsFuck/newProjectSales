package com.projectecommerce.service.payment;

import com.projectecommerce.mapper.PaymentMapper;
import com.projectecommerce.model.dto.request.PaymentRequestDTO;
import com.projectecommerce.model.dto.response.PaymentResponseDTO;
import com.projectecommerce.model.entity.Invoice;
import com.projectecommerce.model.entity.Payment;
import com.projectecommerce.model.enums.InvoiceStatus;
import com.projectecommerce.model.enums.PaymentStatus;
import com.projectecommerce.repository.InvoiceRepository;
import com.projectecommerce.repository.PaymentRepository;
import com.projectecommerce.service.payment.PaymentService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final InvoiceRepository invoiceRepository;
    private final PaymentMapper paymentMapper;

    @Transactional
    @Override
    public PaymentResponseDTO processPayment(PaymentRequestDTO dto) {
        Invoice invoice = invoiceRepository.findById(dto.getInvoiceId())
                .orElseThrow(() -> new EntityNotFoundException("Invoice not found"));

        Payment payment = Payment.builder()
                .invoice(invoice)
                .method(dto.getMethod())
                .amount(dto.getAmount())
                .status(PaymentStatus.SUCCESS)
                .transactionId(UUID.randomUUID().toString())
                .createdAt(LocalDateTime.now())
                .build();

        invoice.setStatus(InvoiceStatus.PAID);
        invoiceRepository.save(invoice);
        return paymentMapper.toDTO(paymentRepository.save(payment));
    }

    @Override
    public PaymentResponseDTO getPaymentDetail(String transactionId) {
        return paymentMapper.toDTO(paymentRepository.findByTransactionId(transactionId)
                .orElseThrow(() -> new EntityNotFoundException("Payment not found")));
    }
}
