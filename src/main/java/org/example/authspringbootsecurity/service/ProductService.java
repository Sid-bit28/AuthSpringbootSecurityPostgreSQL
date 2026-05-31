package org.example.authspringbootsecurity.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.authspringbootsecurity.dto.request.CreateProductRequest;
import org.example.authspringbootsecurity.dto.request.UpdateProductRequest;
import org.example.authspringbootsecurity.dto.response.ProductResponse;
import org.example.authspringbootsecurity.exception.ResourceNotFoundException;
import org.example.authspringbootsecurity.model.Category;
import org.example.authspringbootsecurity.model.Product;
import org.example.authspringbootsecurity.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryService categoryService;

    @Transactional
    public ProductResponse createProduct(CreateProductRequest request) {
        log.info("Creating product with name: {}", request.getName());

        Category category = categoryService.getCategoryEntity(request.getCategoryId());

        Product product = new Product();
        product.setName(request.getName());

        if(request.getDescription() != null && !request.getDescription().trim().isEmpty()) {
            product.setDescription(request.getDescription());
        }

        product.setPrice(request.getPrice());
        product.setQuantity(request.getQuantity());

        if(request.getActive() != null) {
            product.setActive(request.getActive());
        }else product.setActive(true);

        product.setCategory(category);
        log.debug("Product: {}", product);

        Product savedProduct = productRepository.save(product);

        log.info("Product created with id: {}",  savedProduct.getId());

        return ProductResponse.fromEntity(savedProduct);
    }

    @Transactional(readOnly = true)
    public ProductResponse getProduct(Long id) {
        log.info("Getting product with id: {}", id);

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        return ProductResponse.fromEntity(product);
    }

    @Transactional
    public ProductResponse updateProduct(Long id, UpdateProductRequest request) {
        log.info("Updating product with id: {}", id);

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        if(request.getName() != null && !request.getName().trim().isEmpty()) {
            product.setName(request.getName());
        }

        if(request.getDescription() != null && !request.getDescription().trim().isEmpty()) {
            product.setDescription(request.getDescription());
        }

        if(request.getPrice() != null) {
            product.setPrice(request.getPrice());
        }

        if(request.getQuantity() != null) {
            product.setQuantity(request.getQuantity());
        }

        if(request.getActive() != null) {
            product.setActive(request.getActive());
        }

        if(request.getCategoryId() != null) {
            Category category = categoryService.getCategoryEntity(request.getCategoryId());

            product.setCategory(category);
        }

        Product savedProduct =  productRepository.save(product);

        log.info("Product updated successfully");
        return ProductResponse.fromEntity(savedProduct);
    }

    @Transactional
    public void deleteProduct(Long id) {
        log.info("Deleting product with id: {}", id);

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        productRepository.delete(product);
        log.info("Product deleted successfully");
    }

    @Transactional(readOnly = true)
    public Page<ProductResponse> getAllProducts(Pageable pageable) {
        log.info("Fetching all products");

        Page<Product> products = productRepository.findAll(pageable);
        return products.map(ProductResponse::fromEntity);
    }

    @Transactional(readOnly = true)
    public Page<ProductResponse> getProductByCategory(Long categoryId, Pageable pageable) {
        log.info("Fetching products by category with id: {}", categoryId);

        Category category = categoryService.getCategoryEntity(categoryId);

        Page<Product> products = productRepository.findAllByCategory(category, pageable);

        return products.map(ProductResponse::fromEntity);
    }

    @Transactional(readOnly = true)
    public Page<ProductResponse> searchProductsByName(String query, Pageable pageable) {
        log.info("Fetching products by name {}", query);

        Page<Product> product = productRepository.findByNameContainingIgnoreCase(query, pageable);

        return product.map(ProductResponse::fromEntity);
    }

    @Transactional(readOnly = true)
    public Page<ProductResponse> findByPriceRange(BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable) {
        log.info("Fetching products by price range minPrice: {}, maxPrice: {}", minPrice, maxPrice);

        Page<Product> products = productRepository.findByPriceBetween(minPrice, maxPrice, pageable);

        return products.map(ProductResponse::fromEntity);
    }

    @Transactional(readOnly = true)
    public Page<ProductResponse> findProductWithLowStock(Integer threshold, Pageable pageable) {
        log.info("Fetching products with low stock containing quantity less than or equal to : {}", threshold);

        Page<Product> products = productRepository.findLowStockProducts(threshold, pageable);

        return products.map(ProductResponse::fromEntity);
    }

    @Transactional(readOnly = true)
    public Page<ProductResponse> findByCategoryAndPriceRange(Long categoryId, BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable) {
        log.info("Fetching product with category: {} and between the price range {}, {}" , categoryId, minPrice, maxPrice);

        Category category = categoryService.getCategoryEntity(categoryId);

        Page<Product> products = productRepository.findByCategoryAndPriceBetween(category, minPrice, maxPrice, pageable);

        return products.map(ProductResponse::fromEntity);
    }

    public Page<ProductResponse> findActiveProducts(Pageable pageable) {
        log.info("Fetching active products");

        Page<Product> products = productRepository.findByActiveTrue(pageable);

        return products.map(ProductResponse::fromEntity);
    }

    // Helper Function

    @Transactional(readOnly = true)
    public boolean existsById(Long id) {
        return productRepository.existsById(id);
    }
}

