package com.projectecommerce.controller;

import com.projectecommerce.model.dto.request.CategoryDTO;
import com.projectecommerce.model.dto.response.APIResponse;
import com.projectecommerce.model.dto.response.PagedResultDTO;
import com.projectecommerce.model.entity.Category;
import com.projectecommerce.service.category.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<APIResponse<PagedResultDTO<Category>>> getAll(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        PagedResultDTO<Category> result = categoryService.getAllCategories(page - 1, size);
        return ResponseEntity.ok(APIResponse.<PagedResultDTO<Category>>builder()
                .data(result)
                .message("Danh sách danh mục")
                .success(true)
                .timeStamp(LocalDateTime.now())
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<APIResponse<Category>> getById(@PathVariable Long id) {
        Category category = categoryService.getCategoryById(id);
        return ResponseEntity.ok(APIResponse.<Category>builder()
                .data(category)
                .message("Chi tiết danh mục")
                .success(true)
                .timeStamp(LocalDateTime.now())
                .build());
    }

    @PostMapping
    public ResponseEntity<APIResponse<Category>> create(@RequestBody CategoryDTO categoryDTO) {
        Category category = categoryService.createCategory(categoryDTO);
        return ResponseEntity.ok(APIResponse.<Category>builder()
                .data(category)
                .message("Tạo mới danh mục thành công")
                .success(true)
                .timeStamp(LocalDateTime.now())
                .build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<APIResponse<Category>> update(@PathVariable Long id, @RequestBody CategoryDTO categoryDTO) {
        Category updated = categoryService.updateCategory(id, categoryDTO);
        return ResponseEntity.ok(APIResponse.<Category>builder()
                .data(updated)
                .message("Cập nhật danh mục thành công")
                .success(true)
                .timeStamp(LocalDateTime.now())
                .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<APIResponse<String>> delete(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok(APIResponse.<String>builder()
                .data("Xóa mềm danh mục thành công")
                .message("OK")
                .success(true)
                .timeStamp(LocalDateTime.now())
                .build());
    }
}
