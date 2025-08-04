package com.projectecommerce.service.cart;

import com.projectecommerce.model.dto.request.CartRequestDTO;
import com.projectecommerce.model.dto.response.CartResponseDTO;
import com.projectecommerce.model.entity.CartItem;
import com.projectecommerce.model.entity.Product;
import com.projectecommerce.repository.CartItemRepository;
import com.projectecommerce.repository.ProductRepository;
import com.projectecommerce.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Override
    public List<CartResponseDTO> getCartItems(Long userId) {
        return cartItemRepository.findByUserId(userId).stream()
                .map(this::toDTO)
                .toList();
    }

    @Override
    public CartResponseDTO addToCart(Long userId, CartRequestDTO dto) {
        Product product = productRepository.findByIdAndIsDeletedFalse(dto.getProductId())
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));
        CartItem item = cartItemRepository.findByUserIdAndProductId(userId, dto.getProductId())
                .orElse(CartItem.builder()
                        .user(userRepository.getReferenceById(userId))
                        .product(product)
                        .quantity(0)
                        .build());
        item.setQuantity(item.getQuantity() + dto.getQuantity());
        return toDTO(cartItemRepository.save(item));
    }

    @Override
    public CartResponseDTO updateCartItem(Long userId, CartRequestDTO dto) {
        CartItem item = cartItemRepository.findByUserIdAndProductId(userId, dto.getProductId())
                .orElseThrow(() -> new EntityNotFoundException("Cart item not found"));
        item.setQuantity(dto.getQuantity());
        return toDTO(cartItemRepository.save(item));
    }

    private CartResponseDTO toDTO(CartItem item) {
        return CartResponseDTO.builder()
                .id(Long.valueOf(item.getId()))
                .productId(item.getProduct().getId())
                .productName(item.getProduct().getName())
                .quantity(item.getQuantity())
                .price(item.getProduct().getPrice())
                .build();
    }


    @Override
    public void removeCartItem(Long userId, Long productId) {
        cartItemRepository.findByUserIdAndProductId(userId, productId)
                .ifPresent(cartItemRepository::delete);
    }

    @Override
    public void clearCart(Long userId) {
        cartItemRepository.deleteByUserId(userId);
    }
}
