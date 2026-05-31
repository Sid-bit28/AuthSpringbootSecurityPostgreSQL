package org.example.authspringbootsecurity.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.authspringbootsecurity.dto.request.CreateCategoryRequest;
import org.example.authspringbootsecurity.dto.request.UpdateCategoryRequest;
import org.example.authspringbootsecurity.dto.response.CategoryResponse;
import org.example.authspringbootsecurity.exception.DuplicateResourceException;
import org.example.authspringbootsecurity.exception.InvalidOperationException;
import org.example.authspringbootsecurity.exception.ResourceNotFoundException;
import org.example.authspringbootsecurity.model.Category;
import org.example.authspringbootsecurity.repository.CategoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryService {
    private final CategoryRepository categoryRepository;

    @Transactional
    public CategoryResponse createCategory(CreateCategoryRequest request) {
        log.info("Create category with name : {}", request.getName());

        if(categoryRepository.findByName(request.getName()).isPresent()) {
            log.error("Category with name {} already exists", request.getName());
            throw new DuplicateResourceException("Category with name " + request.getName() + " already exists");
        }

        Category category = new Category();
        category.setName(request.getName());
        category.setDescription(request.getDescription());
        category.setActive(true);

        Category savedCategory = categoryRepository.save(category);

        log.info("Category created with id: {}", savedCategory.getId());

        return CategoryResponse.fromEntity(savedCategory);
    }

    @Transactional(readOnly = true)
    public CategoryResponse getCategoryById(Long id) {
        log.info("Get category with id : {}", id);

        Category category = categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No category found with id " + id));
        return CategoryResponse.fromEntity(category);
    }

    @Transactional
    public CategoryResponse updateCategory(Long id, UpdateCategoryRequest request) {
        log.info("Update category with name : {}", request.getName());

        Category category = categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No category found with id " + id));

        if(categoryRepository.findByName(request.getName()).isPresent()) {
            log.error("Category with name {} already exists", request.getName());
            throw new DuplicateResourceException("Category with name " + request.getName() + " already exists");
        }

        if(request.getName() != null && !request.getName().trim().isEmpty()) {
            category.setName(request.getName());
        }

        if(request.getDescription() != null && !request.getDescription().trim().isEmpty()) {
            category.setDescription(request.getDescription());
        }

        if(request.getActive() != null) {
            category.setActive(request.getActive());
        }

        Category savedCategory = categoryRepository.save(category);

        log.info("Category updated with id: {}", savedCategory.getId());

        return CategoryResponse.fromEntity(savedCategory);
    }

    @Transactional
    public void deleteCategory(Long id) {
        log.info("Delete category with id : {}", id);

        Category category = categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Category with id " + id));

        if (!category.getProducts().isEmpty()) {
            log.warn("Delete attempt on category with {} products",
                    category.getProducts().size());
            throw new InvalidOperationException(
                    "Cannot delete category with " + category.getProducts().size() +
                            " products. Delete or reassign products first."
            );
        }

        categoryRepository.delete(category);
        log.info("Category deleted with id: {}", id);
    }

    @Transactional(readOnly = true)
    public Page<CategoryResponse> listCategories(Pageable pageable) {
        log.info("Fetching all categories");
        Page<Category> categories = categoryRepository.findAll(pageable);
        return categories.map(CategoryResponse::fromEntity);
    }

    @Transactional(readOnly = true)
    public Page<CategoryResponse> listActiveCategories(Pageable pageable) {
        log.info("Listing all active categories");
        Page<Category> categories = categoryRepository.findByActiveTrue(pageable);
        return categories.map(CategoryResponse::fromEntity);
    }

    @Transactional(readOnly = true)
    public Page<CategoryResponse> searchCategories(String query, Pageable pageable) {
        log.info("Category search query : {}", query);
        Page<Category> categories = categoryRepository.findByDescriptionContainingIgnoreCase(query, pageable);
        return categories.map(CategoryResponse::fromEntity);
    }

    // Helper Functions
    @Transactional(readOnly = true)
    public Boolean categoryExists(Long id) {
        return categoryRepository.findById(id).isPresent();
    }

    @Transactional(readOnly = true)
    public Category getCategoryEntity(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Category with id " + id + " not found"
                ));
    }
}
