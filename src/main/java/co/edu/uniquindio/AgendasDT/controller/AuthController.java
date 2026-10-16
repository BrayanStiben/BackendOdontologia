package co.edu.uniquindio.AgendasDT.controller;

import co.edu.uniquindio.AgendasDT.dto.AuthResponse;
import co.edu.uniquindio.AgendasDT.dto.LoginRequest;
import co.edu.uniquindio.AgendasDT.dto.RegisterRequest;
import co.edu.uniquindio.AgendasDT.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(request));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<Map<String, String>> forgotPassword(@RequestBody Map<String, String> body) {
        String email = body.getOrDefault("email", "");
        return ResponseEntity.ok(Map.of("message", authService.forgotPassword(email)));
    }
}

