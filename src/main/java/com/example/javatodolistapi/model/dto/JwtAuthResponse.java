package com.example.javatodolistapi.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JwtAuthResponse {
    private String accessToken;
    private final String tokenType = "Bearer";
}
