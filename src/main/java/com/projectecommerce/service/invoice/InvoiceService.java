package com.projectecommerce.service.invoice;

import com.projectecommerce.model.dto.request.PaymentRequestDTO;
import com.projectecommerce.model.dto.response.InvoiceResponseDTO;
import com.projectecommerce.model.dto.response.PaymentResponseDTO;
import com.projectecommerce.model.dto.response.ReportDTO;
import com.projectecommerce.model.enums.InvoiceStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface InvoiceService {
    Page<InvoiceResponseDTO> getInvoices(Pageable pageable, InvoiceStatus status, LocalDate startDate, LocalDate endDate);
    InvoiceResponseDTO getInvoiceDetail(Integer id);
    InvoiceResponseDTO createInvoiceFromOrder(Long orderId);
    InvoiceResponseDTO updateInvoiceStatus(Integer id, InvoiceStatus status);
    InvoiceResponseDTO getInvoiceByOrderId(Long orderId);
    List<ReportDTO> getMonthlyRevenue();
}