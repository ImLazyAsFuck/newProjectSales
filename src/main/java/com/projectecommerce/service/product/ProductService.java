package com.projectecommerce.service.product;

import com.projectecommerce.model.dto.request.ProductRequestDTO;
import com.projectecommerce.model.entity.Product;
import org.springframework.data.domain.Page;

public interface ProductService {
    Page<Product> getProducts(Long categoryId, String search, int page, int size);
    Product getProductDetails(Long productId);
    Product createProduct(ProductRequestDTO dto);
    Product updateProduct(Long id, ProductRequestDTO dto);
    void softDeleteProduct(Long id);
}
