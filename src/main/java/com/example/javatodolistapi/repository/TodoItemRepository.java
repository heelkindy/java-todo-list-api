package com.example.javatodolistapi.repository;

import com.example.javatodolistapi.model.entity.TodoItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface TodoItemRepository extends JpaRepository<TodoItem, Long> {

    Page<TodoItem> findByUserId(Long userId, Pageable pageable);

    Page<TodoItem> findByUserIdAndCompleted(Long userId,boolean completed, Pageable pageable);
}