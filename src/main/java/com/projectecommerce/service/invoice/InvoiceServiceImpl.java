package com.projectecommerce.service.invoice;

import com.projectecommerce.mapper.InvoiceMapper;
import com.projectecommerce.model.dto.response.InvoiceResponseDTO;
import com.projectecommerce.model.dto.response.ReportDTO;
import com.projectecommerce.model.entity.Invoice;
import com.projectecommerce.model.entity.Order;
import com.projectecommerce.model.enums.InvoiceStatus;
import com.projectecommerce.repository.InvoiceRepository;
import com.projectecommerce.repository.OrderRepository;
import com.projectecommerce.service.invoice.InvoiceService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InvoiceServiceImpl implements InvoiceService {
    private final InvoiceRepository invoiceRepository;
    private final OrderRepository orderRepository;
    private final InvoiceMapper invoiceMapper;

    @Override
    public Page<InvoiceResponseDTO> getInvoices(Pageable pageable, InvoiceStatus status, LocalDate start, LocalDate end) {
        return invoiceRepository.filter(status, start, end, pageable)
                .map(invoiceMapper::toDTO);
    }

    @Override
    public InvoiceResponseDTO getInvoiceDetail(Integer id) {
        return invoiceMapper.toDTO(invoiceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Invoice not found")));
    }

    @Transactional
    @Override
    public InvoiceResponseDTO createInvoiceFromOrder(Long orderId) {
        Order order = orderRepository.findById(Math.toIntExact(orderId))
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));

        Invoice invoice = Invoice.builder()
                .order(order)
                .status(InvoiceStatus.UNPAID)
                .totalAmount(order.getTotalPrice())
                .build();

        return invoiceMapper.toDTO(invoiceRepository.save(invoice));
    }

    @Override
    public InvoiceResponseDTO updateInvoiceStatus(Integer id, InvoiceStatus status) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Invoice not found"));
        invoice.setStatus(status);
        return invoiceMapper.toDTO(invoiceRepository.save(invoice));
    }

    @Override
    public InvoiceResponseDTO getInvoiceByOrderId(Long orderId) {
        Invoice invoice = invoiceRepository.findByOrderId(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Invoice not found by order"));
        return invoiceMapper.toDTO(invoice);
    }

    @Override
    public List<ReportDTO> getMonthlyRevenue() {
        return invoiceRepository.getMonthlyRevenue().stream()
                .map(row -> new ReportDTO((Integer) row[0], (Number) row[1]))
                .collect(Collectors.toList());
    }
}
