package com.projectecommerce.controller;

import com.projectecommerce.model.dto.request.CartRequestDTO;
import com.projectecommerce.model.dto.response.APIResponse;
import com.projectecommerce.model.dto.response.CartResponseDTO;
import com.projectecommerce.model.entity.CartItem;
import com.projectecommerce.security.principal.CustomUserDetails;
import com.projectecommerce.service.cart.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<APIResponse<List<CartResponseDTO>>> getCartItems(@AuthenticationPrincipal CustomUserDetails user) {
        List<CartResponseDTO> items = cartService.getCartItems(user.getId());
        return ResponseEntity.ok(APIResponse.<List<CartResponseDTO>>builder()
                .data(items)
                .message("Cart items retrieved")
                .success(true)
                .timeStamp(LocalDateTime.now())
                .build());
    }

    @PostMapping("/items")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<APIResponse<CartResponseDTO>> addToCart(@AuthenticationPrincipal CustomUserDetails user,
                                                                  @RequestBody @Valid CartRequestDTO dto) {
        CartResponseDTO item = cartService.addToCart(user.getId(), dto);
        return ResponseEntity.ok(APIResponse.<CartResponseDTO>builder()
                .data(item)
                .message("Item added to cart")
                .success(true)
                .timeStamp(LocalDateTime.now())
                .build());
    }

    @PutMapping("/items")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<APIResponse<CartResponseDTO>> updateItem(@AuthenticationPrincipal CustomUserDetails user,
                                                                   @RequestBody @Valid CartRequestDTO dto) {
        CartResponseDTO updated = cartService.updateCartItem(user.getId(), dto);
        return ResponseEntity.ok(APIResponse.<CartResponseDTO>builder()
                .data(updated)
                .message("Cart item updated")
                .success(true)
                .timeStamp(LocalDateTime.now())
                .build());
    }

    @DeleteMapping("/items/{productId}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<APIResponse<Void>> deleteItem(@AuthenticationPrincipal CustomUserDetails user,
                                                        @PathVariable Long productId) {
        cartService.removeCartItem(user.getId(), productId);
        return ResponseEntity.ok(APIResponse.<Void>builder()
                .message("Item removed from cart")
                .success(true)
                .timeStamp(LocalDateTime.now())
                .build());
    }

    @DeleteMapping("/clear")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<APIResponse<Void>> clearCart(@AuthenticationPrincipal CustomUserDetails user) {
        cartService.clearCart(user.getId());
        return ResponseEntity.ok(APIResponse.<Void>builder()
                .message("Cart cleared")
                .success(true)
                .timeStamp(LocalDateTime.now())
                .build());
    }
}
