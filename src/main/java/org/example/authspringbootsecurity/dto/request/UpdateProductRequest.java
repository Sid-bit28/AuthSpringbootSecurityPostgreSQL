package org.example.authspringbootsecurity.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateProductRequest {
    @Size(min = 3, max = 10, message = "Product name should be between 3-10 characters")
    private String name;

    @Size(max = 500, message = "Product description should not exceed 500 characters")
    private String description;

    @DecimalMin(value = "0.01", message = "Price should be greater than 0")
    private BigDecimal price;

    @Positive(message = "Product quantity should be positive")
    private Integer quantity;

    private Boolean active;

    private Long categoryId;
}
