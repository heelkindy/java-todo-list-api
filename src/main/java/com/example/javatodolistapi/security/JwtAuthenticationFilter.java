package com.example.javatodolistapi.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailsService customUserDetailsService;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider, CustomUserDetailsService customUserDetailsService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.customUserDetailsService = customUserDetailsService;
    }

    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }

        return null;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        // 1. Get JWT from Request
        String token = getJwtFromRequest(request);

        // 2. Validate Token
        if (StringUtils.hasText(token) && jwtTokenProvider.validateToken(token)) {
            // Get username from token
            String username = jwtTokenProvider.getUsername(token);

            // Download user information
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

            // Create Authentication object
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null, // No password needed when token is available
                    userDetails.getAuthorities()
            );

            // Set authentication details (for non-browser requests)
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            // Set Authentication to Security Context
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // Pass the Request to the next filter
        filterChain.doFilter(request, response);
    }
}
