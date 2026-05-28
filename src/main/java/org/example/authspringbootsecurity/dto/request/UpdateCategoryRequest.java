package org.example.authspringbootsecurity.dto.request;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateCategoryRequest {
    @Size(min = 3, max = 50, message = "Category name must be between 3-50 characters")
    private String name;

    @Size(max = 500, message = "Category description must not exceed 500 characters")
    private String description;

    private Boolean active;
}
