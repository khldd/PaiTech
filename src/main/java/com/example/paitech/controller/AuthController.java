package com.example.paitech.controller;

import com.example.paitech.dto.request.CreateUserRequest;
import com.example.paitech.dto.request.LoginRequest;
import com.example.paitech.dto.response.UserResponse;
import com.example.paitech.model.User;
import com.example.paitech.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    public AuthController(AuthService authService) { this.authService = authService; }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@Valid @RequestBody CreateUserRequest req) {
        return ResponseEntity.ok(authService.register(req));
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@Valid @RequestBody LoginRequest req) {
        String token = authService.login(req);
        return ResponseEntity.ok(Map.of("token", token));
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> me(Principal principal) {
        User user = authService.me(principal);
        return ResponseEntity.ok(authService.toUserResponse(user));
    }
}
