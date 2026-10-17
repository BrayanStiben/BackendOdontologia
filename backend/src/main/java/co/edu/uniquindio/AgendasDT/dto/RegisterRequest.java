package co.edu.uniquindio.AgendasDT.dto;

import co.edu.uniquindio.AgendasDT.domain.enums.UserRol;
import jakarta.validation.constraints.*;

public record RegisterRequest(
        @NotNull(message = "El documento es obligatorio")
        @Positive(message = "El documento debe ser positivo")
        Long id,

        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 100)
        String name,

        @NotBlank(message = "El apellido es obligatorio")
        @Size(max = 100)
        String lastName,

        @NotBlank(message = "El correo es obligatorio")
        @Email(message = "Formato de correo no válido")
        @Size(max = 254)
        String email,

        @NotBlank(message = "El teléfono es obligatorio")
        @Pattern(regexp = "^\\+?[0-9]{7,15}$", message = "Formato de teléfono no válido")
        String phone,

        @NotNull(message = "El rol es obligatorio")
        UserRol rol,

        @Size(max = 50)
        String tp,

        @Size(max = 150)
        String clinicName,

        @NotBlank(message = "La contraseña es obligatoria")
        @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
        String password,

        @NotBlank(message = "Debe confirmar la contraseña")
        String confirmPassword
) { }

