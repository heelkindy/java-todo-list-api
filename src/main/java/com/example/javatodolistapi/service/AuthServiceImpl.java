package com.example.javatodolistapi.service;

import com.example.javatodolistapi.model.dto.LoginRequest;
import com.example.javatodolistapi.model.dto.UserRegistrationRequest;
import com.example.javatodolistapi.model.entity.User;
import com.example.javatodolistapi.repository.UserRepository;
import com.example.javatodolistapi.security.JwtTokenProvider;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           AuthenticationManager authenticationManager,
                           JwtTokenProvider jwtTokenProvider
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public String register(UserRegistrationRequest request) {
        // 1. Check for uniqueness
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already exists!");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already exists!");
        }

        // 2. Create User Entity
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());

        // 3. Encode password before save
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // 4. Save database
        userRepository.save(user);

        return "User registered successfully!";
    }

    @Override
    public String login(LoginRequest request) {
        // 1. Authenticate users using AuthenticationManager
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        // 2. Set the authentication object in the Security Context (Optional but useful)
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 3. Gen JWT
        return jwtTokenProvider.generateToken(authentication); // return token
    }
}
