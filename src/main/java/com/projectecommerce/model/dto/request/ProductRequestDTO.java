package com.projectecommerce.model.dto.request;

import jakarta.validation.constraints.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequestDTO {

    @NotBlank(message = "Tên sản phẩm không được để trống")
    private String name;

    @NotBlank(message = "Mô tả không được để trống")
    private String description;

    @Min(value = 1, message = "Danh mục không hợp lệ")
    @NotNull(message = "Danh mục không hợp lệ")
    private Long categoryId;

    @DecimalMin(value = "0.0", inclusive = false, message = "Giá phải lớn hơn 0")
    @Digits(integer = 10, fraction = 2, message = "Giá không được quá 2 chữ số sau dấu phẩy")
    private double price;

    @Min(value = 0, message = "Tồn kho không được âm")
    private int stock;
}

