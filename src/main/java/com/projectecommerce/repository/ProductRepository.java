package com.projectecommerce.repository;

import com.projectecommerce.model.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long>{
    @Query("SELECT p FROM Product p WHERE p.isDeleted = false and p.category.id=:categoryId")
    boolean existsByCategoryId(Long id);
}
