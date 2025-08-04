package com.projectecommerce.service.category;

import com.projectecommerce.model.dto.request.CategoryDTO;
import com.projectecommerce.model.dto.response.PagedResultDTO;
import com.projectecommerce.model.dto.response.PaginationDTO;
import com.projectecommerce.model.entity.Category;
import com.projectecommerce.repository.CategoryRepository;
import com.projectecommerce.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService{
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    @Override
    public PagedResultDTO<Category> getAllCategories(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Category> categoryPage = categoryRepository.findAll(pageable);

        PaginationDTO pagination = PaginationDTO.builder()
                .currentPage(categoryPage.getNumber() + 1)
                .pageSize(categoryPage.getSize())
                .totalItems(categoryPage.getTotalElements())
                .totalPages(categoryPage.getTotalPages())
                .build();

        return PagedResultDTO.<Category>builder()
                .items(categoryPage.getContent())
                .pagination(pagination)
                .build();
    }

    @Override
    public Category getCategoryById(Long id){
        return categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found with id: " + id));
    }

    @Override
    public Category createCategory(CategoryDTO categoryDTO){
        Category category = Category.builder()
                .name(categoryDTO.getName())
                .description(categoryDTO.getDescription())
                .createAt(LocalDate.now())
                .updateAt(null)
                .build();
        return categoryRepository.save(category);
    }

    @Override
    public Category updateCategory(Long id, CategoryDTO categoryDTO){
        Category foundCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found with id: " + id));
        foundCategory.setName(categoryDTO.getName());
        foundCategory.setDescription(categoryDTO.getDescription());
        foundCategory.setUpdateAt(LocalDate.now());
        return categoryRepository.save(foundCategory);
    }

    @Override
    public void deleteCategory(Long id){
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found with id: " + id));
        if(productRepository.existsByCategoryId(id)){
            throw new IllegalArgumentException("Can't delete category with id: " + id + " because it has products");
        }
        categoryRepository.deleteById(category.getId());
    }
}
