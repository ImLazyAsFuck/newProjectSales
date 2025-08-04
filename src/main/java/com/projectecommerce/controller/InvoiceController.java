package com.projectecommerce.controller;

import com.projectecommerce.model.dto.request.PaymentRequestDTO;
import com.projectecommerce.model.dto.response.APIResponse;
import com.projectecommerce.model.dto.response.InvoiceResponseDTO;
import com.projectecommerce.model.dto.response.PaymentResponseDTO;
import com.projectecommerce.model.dto.response.ReportDTO;
import com.projectecommerce.model.enums.InvoiceStatus;
import com.projectecommerce.service.invoice.InvoiceService;
import com.projectecommerce.service.payment.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/invoices")
@RequiredArgsConstructor
public class InvoiceController {

    private final InvoiceService invoiceService;

    @GetMapping
    public ResponseEntity<APIResponse<Page<InvoiceResponseDTO>>> getInvoices(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) InvoiceStatus status,
            @RequestParam(required = false) LocalDate startDate,
            @RequestParam(required = false) LocalDate endDate
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<InvoiceResponseDTO> data = invoiceService.getInvoices(pageable, status, startDate, endDate);
        return ResponseEntity.ok(
                APIResponse.<Page<InvoiceResponseDTO>>builder()
                        .data(data)
                        .message("Fetched invoices successfully")
                        .success(true)
                        .errors(null)
                        .timeStamp(LocalDateTime.now())
                        .build()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<APIResponse<InvoiceResponseDTO>> getInvoiceDetail(@PathVariable Integer id) {
        InvoiceResponseDTO data = invoiceService.getInvoiceDetail(id);
        return ResponseEntity.ok(
                APIResponse.<InvoiceResponseDTO>builder()
                        .data(data)
                        .message("Invoice detail fetched")
                        .success(true)
                        .errors(null)
                        .timeStamp(LocalDateTime.now())
                        .build()
        );
    }

    @PostMapping
    public ResponseEntity<APIResponse<InvoiceResponseDTO>> createInvoice(@PathVariable Long orderId) {
        InvoiceResponseDTO data = invoiceService.createInvoiceFromOrder(orderId);
        return ResponseEntity.ok(
                APIResponse.<InvoiceResponseDTO>builder()
                        .data(data)
                        .message("Invoice created from order")
                        .success(true)
                        .errors(null)
                        .timeStamp(LocalDateTime.now())
                        .build()
        );
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<APIResponse<InvoiceResponseDTO>> updateStatus(
            @PathVariable Integer id,
            @RequestParam InvoiceStatus status
    ) {
        InvoiceResponseDTO data = invoiceService.updateInvoiceStatus(id, status);
        return ResponseEntity.ok(
                APIResponse.<InvoiceResponseDTO>builder()
                        .data(data)
                        .message("Invoice status updated")
                        .success(true)
                        .errors(null)
                        .timeStamp(LocalDateTime.now())
                        .build()
        );
    }
}
