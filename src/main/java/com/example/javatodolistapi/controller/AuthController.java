package com.example.javatodolistapi.controller;


import com.example.javatodolistapi.model.dto.JwtAuthResponse;
import com.example.javatodolistapi.model.dto.LoginRequest;
import com.example.javatodolistapi.model.dto.UserRegistrationRequest;
import com.example.javatodolistapi.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication API", description = "Handles user authentication")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // Endpoint: POST /api/auth/register
    @PostMapping("/register")
    @Operation(summary = "Register new user", description = "Create a new user account")
    public ResponseEntity<String> registerUser(@Valid @RequestBody UserRegistrationRequest request) {
        String response = authService.register(request);
        return ResponseEntity.ok(response);
    }

    // Endpoint: POST /api/auth/login
    @PostMapping("/login")
    @Operation(summary = "User login", description = "Authenticate user credentials and return token")
    public ResponseEntity<JwtAuthResponse> loginUser(@Valid @RequestBody LoginRequest request) {
        String token = authService.login(request);

        // Return DTO include token and token type (Bearer)
        return ResponseEntity.ok(new JwtAuthResponse(token));
    }
}
