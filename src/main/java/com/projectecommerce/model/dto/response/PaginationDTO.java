package com.projectecommerce.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaginationDTO {
    private int currentPage;
    private int pageSize;
    private int totalPages;
    private long totalItems;
}
