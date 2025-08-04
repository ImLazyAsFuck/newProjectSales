package com.projectecommerce.controller;

import com.projectecommerce.mapper.OrderMapper;
import com.projectecommerce.model.dto.response.*;
import com.projectecommerce.model.entity.Order;
import com.projectecommerce.model.enums.OrderStatus;
import com.projectecommerce.security.principal.CustomUserDetails;
import com.projectecommerce.service.invoice.InvoiceService;
import com.projectecommerce.service.order.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final InvoiceService invoiceService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','SALES','CUSTOMER')")
    public ResponseEntity<APIResponse<PagedResultDTO<OrderResponseDTO>>> listOrders(
            @AuthenticationPrincipal CustomUserDetails user,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        boolean isAdmin = user.hasRole("ADMIN") || user.hasRole("SALES");
        Page<Order> ordersPage = orderService.listOrders(user.getId(), isAdmin, page, size);

        List<OrderResponseDTO> orderDTOs = ordersPage.getContent().stream()
                .map(OrderMapper::mapToDTO)
                .collect(Collectors.toList());

        PagedResultDTO<OrderResponseDTO> result = PagedResultDTO.<OrderResponseDTO>builder()
                .items(orderDTOs)
                .pagination(PaginationDTO.builder()
                        .currentPage(ordersPage.getNumber())
                        .pageSize(ordersPage.getSize())
                        .totalPages(ordersPage.getTotalPages())
                        .totalItems(ordersPage.getTotalElements())
                        .build())
                .build();

        return ResponseEntity.ok(APIResponse.<PagedResultDTO<OrderResponseDTO>>builder()
                .data(result)
                .message("Fetched orders")
                .success(true)
                .timeStamp(LocalDateTime.now())
                .build());
    }


    @GetMapping("/{id}")
    public ResponseEntity<APIResponse<OrderResponseDTO>> getOrder(@AuthenticationPrincipal CustomUserDetails user,
                                                                  @PathVariable Integer id) {
        boolean isAdmin = user.hasRole("ADMIN") || user.hasRole("SALES");
        Order order = orderService.getOrderDetail(id, user.getId(), isAdmin);
        OrderResponseDTO dto = OrderMapper.mapToDTO(order);

        return ResponseEntity.ok(APIResponse.<OrderResponseDTO>builder()
                .data(dto)
                .message("Fetched order")
                .success(true)
                .timeStamp(LocalDateTime.now())
                .build());
    }


    @PostMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<APIResponse<OrderResponseDTO>> createOrder(@AuthenticationPrincipal CustomUserDetails user,
                                                                     @RequestParam String shippingAddress) {
        Order order = orderService.createOrderFromCart(user.getId(), shippingAddress);
        OrderResponseDTO dto = OrderMapper.mapToDTO(order);
        return ResponseEntity.ok(APIResponse.<OrderResponseDTO>builder()
                .data(dto)
                .message("Order created from cart")
                .success(true)
                .timeStamp(LocalDateTime.now())
                .build());
    }


    @PutMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN','SALES')")
    public ResponseEntity<APIResponse<Void>> updateStatus(@PathVariable Integer id,
                                                          @RequestParam String status) {
        orderService.updateOrderStatus(id, OrderStatus.valueOf(status));
        return ResponseEntity.ok(APIResponse.<Void>builder()
                .message("Order status updated")
                .success(true)
                .timeStamp(LocalDateTime.now())
                .build());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<APIResponse<Void>> updateOrderInfo(@AuthenticationPrincipal CustomUserDetails user,
                                                             @PathVariable Integer id,
                                                             @RequestParam String address,
                                                             @RequestParam(required = false) String notes) {
        orderService.updateOrderInfo(id, user.getId(), address, notes);
        return ResponseEntity.ok(APIResponse.<Void>builder()
                .message("Order updated")
                .success(true)
                .timeStamp(LocalDateTime.now())
                .build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<APIResponse<Void>> cancelOrder(@AuthenticationPrincipal CustomUserDetails user,
                                                         @PathVariable Integer id,
                                                         @RequestParam String reason) {
        orderService.cancelOrder(id, user.getId(), reason);
        return ResponseEntity.ok(APIResponse.<Void>builder()
                .message("Order canceled")
                .success(true)
                .timeStamp(LocalDateTime.now())
                .build());
    }

    @GetMapping("/{orderId}/invoice")
    public ResponseEntity<APIResponse<InvoiceResponseDTO>> getInvoiceByOrder(@PathVariable Long orderId) {
        InvoiceResponseDTO data = invoiceService.getInvoiceByOrderId(orderId);
        return ResponseEntity.ok(APIResponse.<InvoiceResponseDTO>builder()
                .data(data)
                .message("Invoice by order fetched")
                .success(true)
                .timeStamp(LocalDateTime.now())
                .build());
    }

}
