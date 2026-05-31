package org.example.authspringbootsecurity.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.authspringbootsecurity.dto.request.CreateCategoryRequest;
import org.example.authspringbootsecurity.dto.request.UpdateCategoryRequest;
import org.example.authspringbootsecurity.dto.response.CategoryResponse;
import org.example.authspringbootsecurity.service.CategoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/categories")
@Validated
@RequiredArgsConstructor
@Slf4j
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryResponse> createCategory(@Valid @RequestBody CreateCategoryRequest request) {
        log.info("Received request to create a new category with name: {}", request.getName());

        CategoryResponse categoryResponse = categoryService.createCategory(request);
        log.info("Created category with id: {}", categoryResponse.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getCategoryById(@PathVariable Long id) {
        log.info("Received request to get category with id: {}", id);
        CategoryResponse categoryResponse = categoryService.getCategoryById(id);
        log.info("Returned category with id: {}", categoryResponse.getId());
        return ResponseEntity.ok(categoryResponse);
    }

    @GetMapping
    public ResponseEntity<Page<CategoryResponse>> getCategories(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) int size,
            @RequestParam(required = false) String sort) {
        log.info("Received request to get all categories");
        Sort sortOrder = Sort.unsorted();
        if(sort != null && !sort.trim().isEmpty()) {
            String[] sortParts = sort.split(",");
            String field = sortParts[0];
            Sort.Direction direction = sortParts.length > 1 && sortParts[1].equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
            sortOrder = Sort.by(direction, field);
        }
        Pageable pageable = PageRequest.of(page, size, sortOrder);
        Page<CategoryResponse> responses = categoryService.listCategories(pageable);
        log.info("Categories retrieved - total: {}, totalPages: {}", responses.getTotalElements(), responses.getTotalPages());
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponse> updateCategory(@PathVariable Long id, @Valid @RequestBody UpdateCategoryRequest request) {
        log.info("Received request to update a category with id: {}", id);
        CategoryResponse categoryResponse = categoryService.updateCategory(id, request);
        log.info("Updated category with id: {}", categoryResponse.getId());
        return ResponseEntity.ok(categoryResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        log.info("Received request to delete a category with id: {}", id);
        categoryService.deleteCategory(id);
        log.info("Deleted category with id: {}", id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }


}
