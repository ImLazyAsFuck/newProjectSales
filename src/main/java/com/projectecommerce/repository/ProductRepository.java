package com.projectecommerce.repository;

import com.projectecommerce.model.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long>{
    @Query("SELECT CASE WHEN COUNT(p) > 0 THEN true ELSE false END FROM Product p WHERE p.isDeleted = false AND p.category.id = :id")
    boolean existsByCategoryId(Long id);

    @Query("SELECT p FROM Product p WHERE p.isDeleted = false AND " +
            "(:categoryId IS NULL OR p.category.id = :categoryId) AND " +
            "(:search IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<Product> searchProducts(@Param("categoryId") Long categoryId,
                                 @Param("search") String search,
                                 Pageable pageable);

    Optional<Product> findByIdAndIsDeletedFalse(Long id);
}
