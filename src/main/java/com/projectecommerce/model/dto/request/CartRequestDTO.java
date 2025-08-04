package com.projectecommerce.model.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartRequestDTO {
    
    @NotNull(message = "ID sản phẩm không được để trống")
    private Long productId;

    @Min(value = 1, message = "Số lượng phải lớn hơn hoặc bằng 1")
    private int quantity;
}
