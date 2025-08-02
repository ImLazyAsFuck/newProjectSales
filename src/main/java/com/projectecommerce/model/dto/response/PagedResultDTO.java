package com.projectecommerce.model.dto.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PagedResultDTO<T> {
    private List<T> items;
    private PaginationDTO pagination;
}
