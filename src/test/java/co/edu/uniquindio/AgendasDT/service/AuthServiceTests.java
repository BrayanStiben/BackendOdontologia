package co.edu.uniquindio.AgendasDT.service;

import co.edu.uniquindio.AgendasDT.domain.User;
import co.edu.uniquindio.AgendasDT.domain.enums.UserRol;
import co.edu.uniquindio.AgendasDT.dto.LoginRequest;
import co.edu.uniquindio.AgendasDT.dto.RegisterRequest;
import co.edu.uniquindio.AgendasDT.exception.UserConflictException;
import co.edu.uniquindio.AgendasDT.repository.UserAuditRepository;
import co.edu.uniquindio.AgendasDT.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTests {
    @Mock UserRepository users;
    @Mock UserAuditRepository audit;
    @Mock PasswordEncoder passwordEncoder;

    AuthService authService;

    @BeforeEach
    void setUp() {
        authService = new AuthService(users, audit, passwordEncoder);
    }

    @Test
    void loginSuccess() {
        User user = new User();
        user.setId(123L);
        user.setName("Alex");
        user.setLastName("Gonzalez");
        user.setEmail("dr.gonzalez@doc-tooth.com");
        user.setPassword("hashed");
        user.setRol(UserRol.Odontologo);
        user.setClinicName("OdontoSmile");

        when(users.findByEmailIgnoreCase("dr.gonzalez@doc-tooth.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("123456", "hashed")).thenReturn(true);

        var res = authService.login(new LoginRequest("dr.gonzalez@doc-tooth.com", "123456", false));

        assertNotNull(res);
        assertEquals(123L, res.id());
        assertEquals("Alex", res.name());
        assertEquals("OdontoSmile", res.clinicName());
        assertNotNull(res.token());
    }

    @Test
    void loginFailsWithBadPassword() {
        User user = new User();
        user.setId(123L);
        user.setPassword("hashed");

        when(users.findByEmailIgnoreCase("dr.gonzalez@doc-tooth.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrong", "hashed")).thenReturn(false);

        assertThrows(BadCredentialsException.class, () ->
                authService.login(new LoginRequest("dr.gonzalez@doc-tooth.com", "wrong", false)));
    }

    @Test
    void registerRejectsPasswordMismatch() {
        var req = new RegisterRequest(1L, "A", "B", "a@b.com", "+573001234567",
                UserRol.Odontologo, "TP-1", "Clinic", "pass1", "pass2");
        assertThrows(UserConflictException.class, () -> authService.register(req));
    }

    @Test
    void registerSuccess() {
        var req = new RegisterRequest(1094821402L, "Alex", "Gonzalez", "alex@doc.com", "+573001234567",
                UserRol.Odontologo, "OD-89421-TP", "OdontoSmile", "password123", "password123");

        when(users.existsById(1094821402L)).thenReturn(false);
        when(users.emailExists("alex@doc.com")).thenReturn(false);
        when(passwordEncoder.encode("password123")).thenReturn("encoded");

        var res = authService.register(req);

        assertNotNull(res);
        assertEquals(1094821402L, res.id());
        verify(users).saveAndFlush(any());
        verify(audit).save(any());
    }
}

