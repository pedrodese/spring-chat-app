package com.github.spring.chat.app.user_module.controller;

import com.github.spring.chat.app.user_module.dto.UserResponseDTO;
import com.github.spring.chat.app.user_module.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> getUserProfile(HttpServletRequest request) {
        UserResponseDTO response = new UserResponseDTO(service.getUserProfileFromToken(request));
        return ResponseEntity.ok(response);
    }

}
