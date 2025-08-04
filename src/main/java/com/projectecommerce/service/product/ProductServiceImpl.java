package com.projectecommerce.service.product;

import com.projectecommerce.model.dto.request.ProductRequestDTO;
import com.projectecommerce.model.entity.Category;
import com.projectecommerce.model.entity.Product;
import com.projectecommerce.repository.CategoryRepository;
import com.projectecommerce.repository.OrderItemRepository;
import com.projectecommerce.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final OrderItemRepository orderItemRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public Page<Product> getProducts(Long categoryId, String search, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return productRepository.searchProducts(categoryId, search, pageable);
    }

    @Override
    public Product getProductDetails(Long productId) {
        return productRepository.findByIdAndIsDeletedFalse(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));
    }


    @Override
    public Product createProduct(ProductRequestDTO dto) {
        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Danh mục không tồn tại"));


        Product product = Product.builder()
                .name(dto.getName().trim())
                .description(dto.getDescription().trim())
                .category(category)
                .price(dto.getPrice())
                .stock(dto.getStock())
                .isDeleted(false)
                .build();

        return productRepository.save(product);
    }

    @Override
    public Product updateProduct(Long id, ProductRequestDTO dto) {
        Product product = getProductDetails(id);

        Category category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Danh mục không tồn tại"));

        product.setName(dto.getName().trim());
        product.setDescription(dto.getDescription().trim());
        product.setCategory(category);
        product.setPrice(dto.getPrice());
        product.setStock(dto.getStock());

        return productRepository.save(product);
    }


    @Override
    public void softDeleteProduct(Long id) {
        Product product = getProductDetails(id);
        boolean hasOrders = orderItemRepository.existsByProductId(product.getId());
        if (hasOrders) {
            throw new IllegalStateException("Cannot delete product with existing orders");
        }
        product.setDeleted(true);
        product.setDeletedAt(LocalDate.now());
        productRepository.save(product);
    }
}
