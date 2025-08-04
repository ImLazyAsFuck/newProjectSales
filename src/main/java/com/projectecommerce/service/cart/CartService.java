package com.projectecommerce.service.cart;

import com.projectecommerce.model.dto.response.CartResponseDTO;
import com.projectecommerce.model.dto.request.CartRequestDTO;

import java.util.List;

public interface CartService {
    List<CartResponseDTO> getCartItems(Long userId);
    CartResponseDTO addToCart(Long userId, CartRequestDTO dto);
    CartResponseDTO updateCartItem(Long userId, CartRequestDTO dto);
    void removeCartItem(Long userId, Long productId);
    void clearCart(Long userId);
}
