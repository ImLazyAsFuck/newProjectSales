package com.projectecommerce.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "catalog")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Category{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    private LocalDate createAt;

    private LocalDate updateAt;

    @PrePersist
    protected void onCreate() {
        this.createAt = LocalDate.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updateAt = LocalDate.now();
    }
}
