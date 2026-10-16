package co.edu.uniquindio.AgendasDT.service;

import co.edu.uniquindio.AgendasDT.domain.User;
import co.edu.uniquindio.AgendasDT.domain.UserAudit;
import co.edu.uniquindio.AgendasDT.dto.AuthResponse;
import co.edu.uniquindio.AgendasDT.dto.LoginRequest;
import co.edu.uniquindio.AgendasDT.dto.RegisterRequest;
import co.edu.uniquindio.AgendasDT.exception.UserConflictException;
import co.edu.uniquindio.AgendasDT.exception.UserNotFoundException;
import co.edu.uniquindio.AgendasDT.repository.UserAuditRepository;
import co.edu.uniquindio.AgendasDT.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.util.Locale;
import java.util.UUID;

@Service
@Validated
@Transactional
public class AuthService {
    private final UserRepository users;
    private final UserAuditRepository audit;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository users, UserAuditRepository audit, PasswordEncoder passwordEncoder) {
        this.users = users;
        this.audit = audit;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthResponse login(@Valid LoginRequest request) {
        String identifier = request.emailOrUsername().strip();
        User user = users.findByEmailIgnoreCase(identifier)
                .or(() -> {
                    try {
                        return users.findById(Long.parseLong(identifier));
                    } catch (NumberFormatException e) {
                        return java.util.Optional.empty();
                    }
                })
                .orElseThrow(() -> new BadCredentialsException("Credenciales inválidas"));

        if (user.getPassword() != null && !passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new BadCredentialsException("Credenciales inválidas");
        }

        String token = UUID.randomUUID().toString();
        return new AuthResponse(
                token,
                user.getId(),
                user.getName(),
                user.getLastName(),
                user.getEmail(),
                user.getRol(),
                user.getClinicName()
        );
    }

    public AuthResponse register(@Valid RegisterRequest request) {
        if (!request.password().equals(request.confirmPassword())) {
            throw new UserConflictException("Las contraseñas no coinciden.");
        }
        String email = request.email().strip().toLowerCase(Locale.ROOT);
        if (users.existsById(request.id())) {
            throw new UserConflictException("Ya existe un usuario con esa identificación.");
        }
        if (users.emailExists(email)) {
            throw new UserConflictException("El correo ya está registrado.");
        }

        User user = new User();
        user.setId(request.id());
        user.setName(request.name().strip());
        user.setLastName(request.lastName().strip());
        user.setEmail(email);
        user.setPhone(request.phone().strip());
        user.setRol(request.rol());
        user.setTp(request.tp() != null ? request.tp().strip() : null);
        user.setClinicName(request.clinicName() != null ? request.clinicName().strip() : null);
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setBirthDate(LocalDate.of(1990, 1, 1)); // Default for clinic registration
        user.setActive(true);

        users.saveAndFlush(user);
        audit.save(new UserAudit(user.getId(), "SELF_REGISTER", "CREATE", null, user.getRol()));

        String token = UUID.randomUUID().toString();
        return new AuthResponse(
                token,
                user.getId(),
                user.getName(),
                user.getLastName(),
                user.getEmail(),
                user.getRol(),
                user.getClinicName()
        );
    }

    public String forgotPassword(String email) {
        String normalized = email.strip().toLowerCase(Locale.ROOT);
        User user = users.findByEmailIgnoreCase(normalized)
                .orElseThrow(() -> new UserNotFoundException("No se encontró usuario con ese correo"));
        return "Se ha enviado un correo con instrucciones a " + user.getEmail();
    }
}

