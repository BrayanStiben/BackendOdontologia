package co.edu.uniquindio.AgendasDT.dto;

import co.edu.uniquindio.AgendasDT.domain.enums.UserRol;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

public record UserUpdateRequest(
        @NotBlank @Size(max = 100) String name,
        @NotBlank @Size(max = 100) String lastName,
        @NotNull @Past LocalDate birthDate,
        @NotBlank @Email @Size(max = 254) String email,
        @NotBlank @Pattern(regexp = "\\+?[0-9]{7,15}") String phone,
        @NotNull UserRol rol,
        @NotNull @PositiveOrZero Long version) {
}
