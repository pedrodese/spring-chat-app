package com.github.spring.chat.app.auth.service;

import com.github.spring.chat.app.auth.dto.AuthResponse;
import com.github.spring.chat.app.auth.dto.LoginRequest;
import com.github.spring.chat.app.auth.dto.RegisterRequest;
import com.github.spring.chat.app.auth.model.User;
import com.github.spring.chat.app.auth.repository.UserRepository;
import com.github.spring.chat.app.common.exception.BadRequestException;
import com.github.spring.chat.app.common.exception.UnauthorizedException;
import com.github.spring.chat.app.common.util.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public AuthResponse register(RegisterRequest request) {
        validateUsernameAlreadyExists(request.username());
        validateEmailAlreadyExists(request.email());

        User user = buildNewUser(request);
        User savedUser = userRepository.save(user);

        String token = jwtUtil.generateToken(savedUser.getId(), savedUser.getUsername());
        return buildAuthResponse(savedUser, token);
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.usernameOrEmail())
                .orElseGet(() -> userRepository.findByEmail(request.usernameOrEmail())
                        .orElseThrow(() -> new UnauthorizedException("Invalid credentials")));

        verifyUserPassword(user, request.password());
        String token = jwtUtil.generateToken(user.getId(), user.getUsername());
        return buildAuthResponse(user, token);
    }

    private User buildNewUser(RegisterRequest request) {
        User user = new User();
        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setDisplayName(request.displayName());
        return user;
    }

    private AuthResponse buildAuthResponse(User user, String token) {
        return new AuthResponse(
                token,
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getDisplayName()
        );
    }

    private void verifyUserPassword(User user, String password) {
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new UnauthorizedException("Invalid credentials");
        }
    }

    private void validateUsernameAlreadyExists(String username) {
        if (userRepository.existsByUsername(username)) {
            throw new BadRequestException("Username is already taken");
        }
    }

    private void validateEmailAlreadyExists(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new BadRequestException("Email is already in use");
        }
    }
}