package com.projectecommerce.controller;

import com.projectecommerce.model.dto.request.ProductRequestDTO;
import com.projectecommerce.model.dto.response.APIResponse;
import com.projectecommerce.model.dto.response.PagedResultDTO;
import com.projectecommerce.model.dto.response.PaginationDTO;
import com.projectecommerce.model.entity.Product;
import com.projectecommerce.service.product.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<APIResponse<PagedResultDTO<Product>>> getProducts(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<Product> resultPage = productService.getProducts(categoryId, search, page, size);

        PagedResultDTO<Product> result = PagedResultDTO.<Product>builder()
                .items(resultPage.getContent())
                .pagination(PaginationDTO.builder()
                        .currentPage(resultPage.getNumber())
                        .pageSize(resultPage.getSize())
                        .totalItems(resultPage.getTotalElements())
                        .totalPages(resultPage.getTotalPages())
                        .build())
                .build();

        return ResponseEntity.ok(APIResponse.<PagedResultDTO<Product>>builder()
                .data(result)
                .message("Fetched products successfully")
                .success(true)
                .timeStamp(LocalDateTime.now())
                .build());
    }


    @GetMapping("/{id}")
    public ResponseEntity<APIResponse<Product>> getProduct(@PathVariable Long id) {
        Product product = productService.getProductDetails(id);
        return ResponseEntity.ok(APIResponse.<Product>builder()
                .data(product)
                .message("Fetched product successfully")
                .success(true)
                .timeStamp(LocalDateTime.now())
                .build());
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','SALES')")
    public ResponseEntity<APIResponse<Product>> create(@Valid @RequestBody ProductRequestDTO dto) {
        Product created = productService.createProduct(dto);
        return ResponseEntity.ok(APIResponse.<Product>builder()
                .data(created)
                .message("Product created")
                .success(true)
                .timeStamp(LocalDateTime.now())
                .build());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','SALES')")
    public ResponseEntity<APIResponse<Product>> update(@PathVariable Long id, @Valid @RequestBody ProductRequestDTO dto) {
        Product updated = productService.updateProduct(id, dto);
        return ResponseEntity.ok(APIResponse.<Product>builder()
                .data(updated)
                .message("Product updated")
                .success(true)
                .timeStamp(LocalDateTime.now())
                .build());
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<APIResponse<Void>> delete(@PathVariable Long id) {
        productService.softDeleteProduct(id);
        return ResponseEntity.ok(APIResponse.<Void>builder()
                .message("Product deleted")
                .success(true)
                .timeStamp(LocalDateTime.now())
                .build());
    }
}
