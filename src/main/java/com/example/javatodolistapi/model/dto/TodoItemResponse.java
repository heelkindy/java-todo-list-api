package com.example.javatodolistapi.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TodoItemResponse {
    private Long id;
    private String title;
    private String description;
    private boolean completed;
    // You can add userId or username for easy tracking
    private String username;
}
