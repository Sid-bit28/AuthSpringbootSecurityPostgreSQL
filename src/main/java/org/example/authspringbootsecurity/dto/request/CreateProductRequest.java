package org.example.authspringbootsecurity.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateProductRequest {
    @NotBlank(message = "Category name is required")
    @Size(min = 2, max = 100, message = "Product name should be between 2-100 characters")
    private String name;

    @Size(max = 500, message = "Product description should not exceed 500 characters")
    private String description;

    @NotNull(message = "Price of the product is required")
    @DecimalMin(value = "0.01", message = "Product price must be greater than 0")
    private BigDecimal price;

    @NotNull(message = "Quantity is required")
    @Positive(message = "Quantity of the product must be positive")
    private Integer quantity;

    @NotNull(message = "Category ID is required")
    private Long categoryId;

    private Boolean active;
}
