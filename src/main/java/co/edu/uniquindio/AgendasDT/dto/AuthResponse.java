package co.edu.uniquindio.AgendasDT.dto;

import co.edu.uniquindio.AgendasDT.domain.enums.UserRol;

public record AuthResponse(
        String token,
        Long id,
        String name,
        String lastName,
        String email,
        UserRol rol,
        String clinicName
) { }

