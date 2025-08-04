package com.projectecommerce.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "products")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
public class Product{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private int stock;

    @Column(nullable = false, columnDefinition = "BIT")
    private boolean isDeleted;

    @Column(nullable = false)
    private double price;

    private LocalDate deletedAt;

    private LocalDate createdAt;

    private LocalDate updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDate.now();
        this.updatedAt = LocalDate.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDate.now();
    }

    @PreRemove
    protected void onDelete() {
        this.isDeleted = true;
        this.deletedAt = LocalDate.now();
    }

    @PostLoad
    protected void onLoad() {
        this.isDeleted = false;
        this.deletedAt = null;
    }
}
