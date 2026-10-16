package co.edu.uniquindio.AgendasDT.controller;

import co.edu.uniquindio.AgendasDT.config.MethodSecurityConfig;
import co.edu.uniquindio.AgendasDT.domain.enums.UserRol;
import co.edu.uniquindio.AgendasDT.dto.AuthResponse;
import co.edu.uniquindio.AgendasDT.dto.LoginRequest;
import co.edu.uniquindio.AgendasDT.dto.RegisterRequest;
import co.edu.uniquindio.AgendasDT.exception.ApiExceptionHandler;
import co.edu.uniquindio.AgendasDT.service.AuthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@Import({co.edu.uniquindio.AgendasDT.config.SecurityConfig.class, MethodSecurityConfig.class, ApiExceptionHandler.class})
class AuthControllerTests {
    @Autowired MockMvc mvc;
    @MockitoBean AuthService authService;

    @Test
    void loginReturnsAuthResponse() throws Exception {
        when(authService.login(any(LoginRequest.class))).thenReturn(
                new AuthResponse("token-xyz", 100L, "Alex", "Gonzalez", "alex@doc.com", UserRol.Odontologo, "OdontoSmile")
        );

        mvc.perform(post("/api/auth/login").with(csrf())
                        .contentType("application/json")
                        .content("""
                        {"emailOrUsername":"alex@doc.com","password":"secretpassword","rememberMe":false}
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("token-xyz"))
                .andExpect(jsonPath("$.name").value("Alex"));
    }

    @Test
    void registerReturnsCreated() throws Exception {
        when(authService.register(any(RegisterRequest.class))).thenReturn(
                new AuthResponse("token-xyz", 100L, "Alex", "Gonzalez", "alex@doc.com", UserRol.Odontologo, "OdontoSmile")
        );

        mvc.perform(post("/api/auth/register").with(csrf())
                        .contentType("application/json")
                        .content("""
                        {"id":100,"name":"Alex","lastName":"Gonzalez","email":"alex@doc.com",
                         "phone":"+573001234567","rol":"Odontologo","tp":"OD-123","clinicName":"OdontoSmile",
                         "password":"secretpassword","confirmPassword":"secretpassword"}
                        """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.clinicName").value("OdontoSmile"));
    }
}
