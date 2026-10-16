package co.edu.uniquindio.AgendasDT.controller;

import co.edu.uniquindio.AgendasDT.config.MethodSecurityConfig;
import co.edu.uniquindio.AgendasDT.dto.ClinicalHistoryResponse;
import co.edu.uniquindio.AgendasDT.dto.PatientClinicalProfileResponse;
import co.edu.uniquindio.AgendasDT.exception.ApiExceptionHandler;
import co.edu.uniquindio.AgendasDT.service.ClinicalHistoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Collections;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ClinicalHistoryController.class)
@Import({MethodSecurityConfig.class, ApiExceptionHandler.class})
class ClinicalHistoryControllerTests {
    @Autowired MockMvc mvc;
    @MockitoBean ClinicalHistoryService historyService;

    @Test
    @WithMockUser(roles = "Odontologo")
    void getProfileReturnsPatientData() throws Exception {
        when(historyService.getPatientProfile(eq(100L))).thenReturn(
                new PatientClinicalProfileResponse(100L, "Maria", "Garcia", LocalDate.of(1995, 5, 12),
                        "+573001234567", "maria@doc.com", Collections.emptyList())
        );

        mvc.perform(get("/api/clinical-histories/patient/100"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Maria"));
    }

    @Test
    @WithMockUser(roles = "Odontologo")
    void addEvolutionCreatesRecord() throws Exception {
        UUID id = UUID.randomUUID();
        when(historyService.addEvolution(any())).thenReturn(
                new ClinicalHistoryResponse(id, Instant.now(), "#16 (Molar)", "Resina", "Caries", "OK", "Dr. Alex", true)
        );

        mvc.perform(post("/api/clinical-histories/evolution").with(csrf())
                        .contentType("application/json")
                        .content("""
                        {"patientId":100,"doctorId":200,"toothPiece":"#16 (Molar)",
                         "procedureName":"Resina","diagnosis":"Caries","observations":"OK"}
                        """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.toothPiece").value("#16 (Molar)"))
                .andExpect(jsonPath("$.signed").value(true));
    }
}

