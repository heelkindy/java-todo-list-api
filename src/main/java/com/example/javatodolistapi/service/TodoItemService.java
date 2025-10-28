package com.example.javatodolistapi.service;

import com.example.javatodolistapi.model.dto.TodoItemRequest;
import com.example.javatodolistapi.model.dto.TodoItemResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TodoItemService {
    TodoItemResponse createTodo (TodoItemRequest request, String username);

    TodoItemResponse getTodoById(Long id, String username);

    Page<TodoItemResponse> getTodosByUserId(String username, Boolean completed, Pageable pageable);

    TodoItemResponse updateTodo(Long id, TodoItemRequest request, String username);

    void deleteTodo(Long id, String username);
}
