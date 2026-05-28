package org.example.authspringbootsecurity.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateCategoryRequest {
    @NotBlank(message = "Category name is required.")
    @Size(min = 2, max = 100, message = "Category name should be between 2-100 characters")
    private String name;

    @Size(max = 500, message = "Category description should not exceed 500 characters")
    private String description;
}
