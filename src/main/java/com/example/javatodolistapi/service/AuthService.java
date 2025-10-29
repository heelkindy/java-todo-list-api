package com.example.javatodolistapi.service;

import com.example.javatodolistapi.model.dto.LoginRequest;
import com.example.javatodolistapi.model.dto.UserRegistrationRequest;

public interface AuthService {
    String register(UserRegistrationRequest request);

    String login(LoginRequest request);
}
