package co.edu.uniquindio.AgendasDT.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "El correo o usuario es requerido") String emailOrUsername,
        @NotBlank(message = "La contraseña es requerida") String password,
        Boolean rememberMe
) { }

