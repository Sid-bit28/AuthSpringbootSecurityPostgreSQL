package org.example.authspringbootsecurity.repository;

import org.example.authspringbootsecurity.model.Category;
import org.example.authspringbootsecurity.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query(
            value = "SELECT p FROM Product p JOIN FETCH p.category WHERE p.category = :category AND p.active = true",
            countQuery = "SELECT COUNT(p) FROM Product p WHERE p.category = :category AND p.active = true"
    )
    Page<Product> findAllByCategory(@Param("category") Category category, Pageable pageable);

    Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable);

    Page<Product> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.quantity <= :threshold ORDER BY p.quantity ASC, p.id ASC")
    Page<Product> findLowStockProducts(@Param("threshold") Integer threshold, Pageable pageable);

    Page<Product> findByCategoryAndPriceBetween(Category category, BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);

    Page<Product> findByActiveTrue(Pageable pageable);
}
