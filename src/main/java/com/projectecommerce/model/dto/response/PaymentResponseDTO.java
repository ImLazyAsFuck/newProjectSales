package com.projectecommerce.model.dto.response;

import com.projectecommerce.model.enums.PaymentMethod;
import com.projectecommerce.model.enums.PaymentStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentResponseDTO {
    private Integer id;
    private Integer invoiceId;
    private PaymentMethod method;
    private BigDecimal amount;
    private PaymentStatus status;
    private String transactionId;
    private LocalDateTime createdAt;
}