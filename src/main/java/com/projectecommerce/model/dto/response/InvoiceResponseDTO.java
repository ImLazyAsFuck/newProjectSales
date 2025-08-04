package com.projectecommerce.model.dto.response;

import com.projectecommerce.model.enums.InvoiceStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvoiceResponseDTO {
    private Integer id;
    private Long orderId;
    private InvoiceStatus status;
    private BigDecimal totalAmount;
    private LocalDate createdAt;
    private LocalDate updatedAt;
}
