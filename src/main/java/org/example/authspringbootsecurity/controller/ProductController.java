package org.example.authspringbootsecurity.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.authspringbootsecurity.dto.request.CreateProductRequest;
import org.example.authspringbootsecurity.dto.request.UpdateProductRequest;
import org.example.authspringbootsecurity.dto.response.ProductResponse;
import org.example.authspringbootsecurity.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@Controller
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Validated
@Slf4j
public class ProductController {
    private ProductService productService;

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody CreateProductRequest request) {
        log.info("Creating product request with name: {}", request.getName());
        ProductResponse response = productService.createProduct(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<ProductResponse>> getAllProducts(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) int size,
            @RequestParam(required = false) String query
    ) {
        log.info("Retrieving all products with page: {}, size: {}", page, size);
        Sort sortOrder = Sort.unsorted();
        if(query != null && !query.trim().isEmpty()) {
            String[] sortParts = query.split(",");
            String field = sortParts[0];
            Sort.Direction direction = sortParts.length > 1 && sortParts[1].equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
            sortOrder = Sort.by(direction, field);
        }
        Pageable pageable = PageRequest.of(page, size, sortOrder);
        Page<ProductResponse> responses = productService.getAllProducts(pageable);
        log.info("Fetched products with responses: {}", responses);
        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable Long id) {
        log.info("Retrieving product with id: {}", id);
        ProductResponse response = productService.getProduct(id);
        log.info("Product retrieved successfully")
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/by-category/{categoryId}")
    public ResponseEntity<Page<ProductResponse>> getProductById(
            @PathVariable Long categoryId,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) int size,
            @RequestParam(required = false) String query) {
        log.info("Fetching all products with categoryId: {}", categoryId);
        Sort sortOrder = Sort.unsorted();
        if(query != null && !query.trim().isEmpty()) {
            String[] sortParts = query.split(",");
            String field = sortParts[0];
            Sort.Direction direction = sortParts.length > 1 && sortParts[1].equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
            sortOrder = Sort.by(direction, field);
        }
        Pageable pageable = PageRequest.of(page, size, sortOrder);
        Page<ProductResponse> responses = productService.getProductByCategory(categoryId, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }

    @GetMapping("/price-range")
    public ResponseEntity<Page<ProductResponse>> getProductByPriceRange(
            @RequestParam @DecimalMin("0.01") BigDecimal min,
            @RequestParam @DecimalMin("0.01") BigDecimal max,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) int size,
            @RequestParam(required = false) String sort
    ) {
        log.info("Finding products in price range: {} to {}", min, max);

        Sort sortOrder = Sort.unsorted();
        if (sort != null && !sort.isEmpty()) {
            String[] sortParts = sort.split(",");
            Sort.Direction direction = sortParts.length > 1 && sortParts[1].equalsIgnoreCase("desc")
                    ? Sort.Direction.DESC
                    : Sort.Direction.ASC;
            sortOrder = Sort.by(direction, sortParts[0]);
        }

        Pageable pageable = PageRequest.of(page, size, sortOrder);
        Page<ProductResponse> responses = productService.findByPriceRange(min, max, pageable);
        log.info("Found {} products in price range", responses.getTotalElements());

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/low-stock")
    public ResponseEntity<Page<ProductResponse>> findLowStockProducts(
            @RequestParam @Min(1) int threshold,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Min(1) int size) {

        log.info("Finding low stock products with threshold: {}", threshold);

        Pageable pageable = PageRequest.of(page, size);
        Page<ProductResponse> responses = productService.findProductWithLowStock(threshold, pageable);
        log.info("Found {} low stock products", responses.getTotalElements());

        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody UpdateProductRequest request) {

        log.info("Updating product with id: {}", id);
        ProductResponse response = productService.updateProduct(id, request);
        log.info("Product updated successfully");

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        log.info("Deleting product with id: {}", id);
        productService.deleteProduct(id);
        log.info("Product deleted successfully");
        return ResponseEntity.noContent().build();
    }
}
