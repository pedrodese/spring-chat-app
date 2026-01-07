package com.github.spring.chat.app.user_module.service;

import com.github.spring.chat.app.auth_module.model.User;
import com.github.spring.chat.app.auth_module.repository.UserRepository;
import com.github.spring.chat.app.common_module.exception.ResourceNotFoundException;
import com.github.spring.chat.app.common_module.util.JwtUtil;
import com.github.spring.chat.app.config_module.JwtAuthenticationFilter;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository repository;
    private final JwtUtil jwtUtil;
    private final JwtAuthenticationFilter filter;

    public UserService(UserRepository repository, JwtUtil jwtUtil, JwtAuthenticationFilter filter) {
        this.repository = repository;
        this.jwtUtil = jwtUtil;
        this.filter = filter;
    }

    public User getUserProfileFromToken(HttpServletRequest request) {
        String token = filter.getJwtFromRequest(request);
        return repository.findById(jwtUtil.getUserIdFromToken(token))
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    public User getUserById(String id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}
