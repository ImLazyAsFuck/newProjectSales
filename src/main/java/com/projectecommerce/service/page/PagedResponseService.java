package com.projectecommerce.service.page;

import com.projectecommerce.model.dto.response.PaginationDTO;
import com.projectecommerce.model.dto.response.PagedResultDTO;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class PagedResponseService {

    public <T, U> PagedResultDTO<U> build(Page<T> page, Function<T, U> mapper) {
        List<U> items = page.getContent().stream()
                .map(mapper)
                .collect(Collectors.toList());

        PaginationDTO pagination = PaginationDTO.builder()
                .currentPage(page.getNumber())
                .pageSize(page.getSize())
                .totalPages(page.getTotalPages())
                .totalItems(page.getTotalElements())
                .build();

        return PagedResultDTO.<U>builder()
                .items(items)
                .pagination(pagination)
                .build();
    }
}
