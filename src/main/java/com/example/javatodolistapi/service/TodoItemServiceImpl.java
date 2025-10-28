package com.example.javatodolistapi.service;

import com.example.javatodolistapi.model.dto.TodoItemRequest;
import com.example.javatodolistapi.model.dto.TodoItemResponse;
import com.example.javatodolistapi.model.entity.TodoItem;
import com.example.javatodolistapi.model.entity.User;
import com.example.javatodolistapi.repository.TodoItemRepository;
import com.example.javatodolistapi.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class TodoItemServiceImpl implements TodoItemService {

    private final TodoItemRepository todoItemRepository;
    private final UserRepository userRepository;

    public TodoItemServiceImpl(TodoItemRepository todoItemRepository, UserRepository userRepository) {
        this.todoItemRepository = todoItemRepository;
        this.userRepository = userRepository;
    }

    // --- Support methods ---

    // Utility function to check user and find TodoItem
    private TodoItem findTodoAndVerifyUser(Long todoId, String username) {
        TodoItem todo = todoItemRepository.findById(todoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Todo not found with id " + todoId));

        if (!todo.getUser().getUsername().equals(username)) {
            // Required: Implement user authentication and error handling
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied. Not the owner of this Todo.");
        }
        return todo;
    }

    // Utility function to map Entity to Response DTO
    private TodoItemResponse mapToResponse(TodoItem todo) {
        return new TodoItemResponse(
                todo.getId(),
                todo.getTitle(),
                todo.getDescription(),
                todo.isCompleted(),
                todo.getUser().getUsername()
        );
    }

    // --- Implement CRUD Logic ---

    @Override
    public TodoItemResponse createTodo(TodoItemRequest request, String username) {
        // 1. Find Current user
        User user = userRepository.findByUsername(username).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with username " + username));

        // 2. Map Request DTO to Entity
        TodoItem todo = new TodoItem();
        todo.setTitle(request.getTitle());
        todo.setDescription(request.getDescription());
        todo.setCompleted(request.getCompleted());
        todo.setUser(user); // Link TodoItem with current user

        // 3. Saved in Database
        TodoItem savedTodo = todoItemRepository.save(todo);

        // 4. Return Response DTO
        return mapToResponse(savedTodo);
    }

    @Override
    public TodoItemResponse getTodoById(Long id, String username) {
        // Find TodoItem and check role owner
        TodoItem todo = findTodoAndVerifyUser(id, username);
        return mapToResponse(todo);
    }

    @Override
    public Page<TodoItemResponse> getTodosByUserId(String username, Boolean completed, Pageable pageable) {
        // 1. Find user
        User user = userRepository.findByUsername(username).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with username " + username));

        Page<TodoItem> todoItemPage;

        if (completed != null) {
            // add filter with completed status
            todoItemPage = todoItemRepository.findByUserIdAndCompleted(user.getId(), completed, pageable);
        } else {
            // Only page size
            todoItemPage = todoItemRepository.findByUserId(user.getId(), pageable);
        }

        // 3. Return Response DTO
        return todoItemPage.map(this::mapToResponse);
    }

    @Override
    public TodoItemResponse updateTodo(Long id, TodoItemRequest request, String username) {
        // Find TodoItem and check role owner
        TodoItem todo = findTodoAndVerifyUser(id, username);

        // Update
        todo.setTitle(request.getTitle());
        todo.setDescription(request.getDescription());
        todo.setCompleted(request.getCompleted());

        TodoItem updatedTodo = todoItemRepository.save(todo);
        return mapToResponse(updatedTodo);
    }

    @Override
    public void deleteTodo(Long id, String username) {
        // Find TodoItem and check role owner
        TodoItem todo = findTodoAndVerifyUser(id, username);
        // Xóa
        todoItemRepository.delete(todo);
    }

}
