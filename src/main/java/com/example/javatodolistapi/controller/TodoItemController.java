package com.example.javatodolistapi.controller;

import com.example.javatodolistapi.model.dto.TodoItemRequest;
import com.example.javatodolistapi.model.dto.TodoItemResponse;
import com.example.javatodolistapi.service.TodoItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/todos")
@Tag(name = "TodoItem API", description = "Manage user's todo tasks within the application")
public class TodoItemController {
    private final TodoItemService todoItemService;

    public TodoItemController(TodoItemService todoItemService) {
        this.todoItemService = todoItemService;
    }

    // Endpoint: POST /api/todos
    @PostMapping
    @Operation(summary = "Create a new todo", description = "Create a new todo item for the logged-in user")
    public ResponseEntity<TodoItemResponse> createTodoItem(@Valid @RequestBody TodoItemRequest request, Principal principal) {
        // @Valid for start Data Validation from DTO
        // principal.getName() get username of user login
        TodoItemResponse response = todoItemService.createTodo(request, principal.getName());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // Endpoint: GET /api/todos/{id} (Read One)
    @GetMapping("/{id}")
    @Operation(summary = "Get a todo by ID", description = "Retrieve a specific todo item by its unique ID")
    public ResponseEntity<TodoItemResponse> getTodoById(@PathVariable Long id, Principal principal) {
        TodoItemResponse response = todoItemService.getTodoById(id, principal.getName());
        return ResponseEntity.ok(response);
    }

    // Endpoint: GET /api/todos (Read All - Pagination & Filtering)
    // Ex URL: /api/todos?page=0&size=10&completed=true&sort=id,desc
    @GetMapping
    @Operation(summary = "Get all todos", description = "Retrieve a paginated list of all todos for the current user, optionally filtered by completion status")
    public ResponseEntity<Page<TodoItemResponse>> getTodos(
            @RequestParam(required = false) Boolean completed,
            // @PageableDefault allows setting defaults for pagination
            @PageableDefault(size = 10, sort = "id") Pageable pageable,
            Principal principal) {

        Page<TodoItemResponse> page = todoItemService.getTodosByUserId(
                principal.getName(),
                completed,
                pageable
        );
        return ResponseEntity.ok(page);
    }

    // Endpoint: PUT /api/todos/{id} (Update)
    @PutMapping("/{id}")
    @Operation(summary = "Update a todo", description = "Update an existing todo by its ID")
    public ResponseEntity<TodoItemResponse> updateTodo(
            @PathVariable Long id,
            @Valid @RequestBody TodoItemRequest request,
            Principal principal) {

        TodoItemResponse response = todoItemService.updateTodo(id, request, principal.getName());
        return ResponseEntity.ok(response);
    }

    // Endpoint: DELETE /api/todos/{id} (Delete)
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a todo", description = "Delete a todo item by its ID")
    public ResponseEntity<Void> deleteTodo(@PathVariable Long id, Principal principal) {
        todoItemService.deleteTodo(id, principal.getName());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT); // Return 204 No Content
    }
}
