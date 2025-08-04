package com.projectecommerce.mapper;

import com.projectecommerce.model.dto.response.InvoiceResponseDTO;
import com.projectecommerce.model.entity.Invoice;
import org.springframework.stereotype.Component;

@Component
public class InvoiceMapper {
    public InvoiceResponseDTO toDTO(Invoice invoice) {
        return InvoiceResponseDTO.builder()
                .id(invoice.getId())
                .orderId(Long.valueOf(invoice.getOrder().getId()))
                .status(invoice.getStatus())
                .totalAmount(invoice.getTotalAmount())
                .createdAt(invoice.getCreatedAt())
                .updatedAt(invoice.getUpdatedAt())
                .build();
    }
}
