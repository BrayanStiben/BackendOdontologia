package co.edu.uniquindio.AgendasDT.dto;

import co.edu.uniquindio.AgendasDT.domain.User;
import co.edu.uniquindio.AgendasDT.domain.enums.UserRol;
import java.time.LocalDate;

public record UserResponse(Long id, String name, String lastName, LocalDate birthDate,
                           String email, String phone, UserRol rol, Long version) {
    public static UserResponse from(User user) {
        return new UserResponse(user.getId(), user.getName(), user.getLastName(),
                user.getBirthDate(), user.getEmail(), user.getPhone(), user.getRol(), user.getVersion());
    }
}
