package com.projectecommerce.model.dto.request;

import com.projectecommerce.model.enums.PaymentMethod;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentRequestDTO {
    @NotNull
    private Integer invoiceId;

    @NotNull
    private PaymentMethod method;

    @DecimalMin("0.0")
    @NotNull
    private BigDecimal amount;
}