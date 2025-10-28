package com.example.javatodolistapi.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class TodoItemRequest {
    @NotBlank(message = "Title cannot be empty")
    @Size(min = 1, max = 100, message = "Title must be between 1 and 100 characters")
    private String title;

    private String description;

    // When updating, the client can send this fieldnày
    @NotNull(message = "Completed status is required")
    private Boolean completed;
}
