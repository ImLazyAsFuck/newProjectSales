package com.projectecommerce.service.category;

import com.projectecommerce.model.dto.request.CategoryDTO;
import com.projectecommerce.model.dto.response.APIResponse;
import com.projectecommerce.model.dto.response.PagedResultDTO;
import com.projectecommerce.model.entity.Category;

public interface CategoryService {
    PagedResultDTO<Category> getAllCategories(int page, int size);
    Category getCategoryById(Long id);
    Category createCategory(CategoryDTO category);
    Category updateCategory(Long id, CategoryDTO categoryDTO);
    void deleteCategory(Long id);
}
