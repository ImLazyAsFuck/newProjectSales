package com.projectecommerce.controller;

import com.projectecommerce.model.dto.request.PaymentRequestDTO;
import com.projectecommerce.model.dto.response.APIResponse;
import com.projectecommerce.model.dto.response.PaymentResponseDTO;
import com.projectecommerce.service.payment.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<APIResponse<PaymentResponseDTO>> processPayment(@Valid @RequestBody PaymentRequestDTO dto) {
        PaymentResponseDTO data = paymentService.processPayment(dto);
        return ResponseEntity.ok(
                APIResponse.<PaymentResponseDTO>builder()
                        .data(data)
                        .message("Payment processed")
                        .success(true)
                        .errors(null)
                        .timeStamp(LocalDateTime.now())
                        .build()
        );
    }

    @GetMapping("/{transactionId}")
    public ResponseEntity<APIResponse<PaymentResponseDTO>> getPaymentDetail(@PathVariable String transactionId) {
        PaymentResponseDTO data = paymentService.getPaymentDetail(transactionId);
        return ResponseEntity.ok(
                APIResponse.<PaymentResponseDTO>builder()
                        .data(data)
                        .message("Payment detail fetched")
                        .success(true)
                        .errors(null)
                        .timeStamp(LocalDateTime.now())
                        .build()
        );
    }
}
