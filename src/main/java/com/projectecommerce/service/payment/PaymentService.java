package com.projectecommerce.service.payment;

import com.projectecommerce.model.dto.request.PaymentRequestDTO;
import com.projectecommerce.model.dto.response.PaymentResponseDTO;

public interface PaymentService {
    PaymentResponseDTO processPayment(PaymentRequestDTO dto);
    PaymentResponseDTO getPaymentDetail(String transactionId);
}
