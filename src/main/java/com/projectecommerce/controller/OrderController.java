package com.projectecommerce.controller;

import com.projectecommerce.mapper.OrderMapper;
import com.projectecommerce.model.dto.response.APIResponse;
import com.projectecommerce.model.dto.response.OrderResponseDTO;
import com.projectecommerce.model.dto.response.PagedResultDTO;
import com.projectecommerce.model.dto.response.PaginationDTO;
import com.projectecommerce.model.entity.Order;
import com.projectecommerce.model.enums.OrderStatus;
import com.projectecommerce.security.principal.CustomUserDetails;
import com.projectecommerce.service.order.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','SALES','CUSTOMER')")
    public ResponseEntity<APIResponse<PagedResultDTO<Order>>> listOrders(@AuthenticationPrincipal CustomUserDetails user,
                                                                         @RequestParam(defaultValue = "0") int page,
                                                                         @RequestParam(defaultValue = "10") int size) {
        boolean isAdmin = user.hasRole("ADMIN") || user.hasRole("SALES");
        Page<Order> ordersPage = orderService.listOrders(user.getId(), isAdmin, page, size);

        PagedResultDTO<Order> result = PagedResultDTO.<Order>builder()
                .items(ordersPage.getContent())
                .pagination(PaginationDTO.builder()
                        .currentPage(ordersPage.getNumber())
                        .pageSize(ordersPage.getSize())
                        .totalPages(ordersPage.getTotalPages())
                        .totalItems(ordersPage.getTotalElements())
                        .build())
                .build();

        return ResponseEntity.ok(APIResponse.<PagedResultDTO<Order>>builder()
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
    public ResponseEntity<APIResponse<Order>> createOrder(@AuthenticationPrincipal CustomUserDetails user,
                                                          @RequestParam String shippingAddress) {
        Order order = orderService.createOrderFromCart(user.getId(), shippingAddress);
        return ResponseEntity.ok(APIResponse.<Order>builder()
                .data(order)
                .message("Order created from cart")
                .success(true)
                .timeStamp(LocalDateTime.now())
                .build());
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN','SALES')")
    public ResponseEntity<APIResponse<Void>> updateStatus(@PathVariable Integer id,
                                                          @RequestParam OrderStatus status) {
        orderService.updateOrderStatus(id, status);
        return ResponseEntity.ok(APIResponse.<Void>builder()
                .message("Order status updated")
                .success(true)
                .timeStamp(LocalDateTime.now())
                .build());
    }
}
