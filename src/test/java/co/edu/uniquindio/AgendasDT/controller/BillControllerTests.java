package co.edu.uniquindio.AgendasDT.controller;

import co.edu.uniquindio.AgendasDT.config.MethodSecurityConfig;
import co.edu.uniquindio.AgendasDT.dto.BillResponse;
import co.edu.uniquindio.AgendasDT.exception.ApiExceptionHandler;
import co.edu.uniquindio.AgendasDT.service.BillService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
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

@WebMvcTest(BillController.class)
@Import({MethodSecurityConfig.class, ApiExceptionHandler.class})
class BillControllerTests {
    @Autowired MockMvc mvc;
    @MockitoBean BillService billService;

    @Test
    @WithMockUser(roles = "Recepcionista")
    void createBillReturnsCreated() throws Exception {
        UUID id = UUID.randomUUID();
        when(billService.create(any())).thenReturn(
                new BillResponse(id, "FE-2026-0492", Instant.now(), 100L, "Maria Garcia", 100L,
                        "Cra 15 #22-10", "Tarjeta de Credito", 280000.0, 14000.0, 0.0, 266000.0,
                        "DOSCIENTOS SESENTA Y SEIS MIL", "cufe-abc", "Res. DIAN", "qr-data",
                        "Tratamiento", Collections.emptyList())
        );

        mvc.perform(post("/api/bills").with(csrf())
                        .contentType("application/json")
                        .content("""
                        {"patientId":100,"paymentMethod":"Tarjeta de Credito","address":"Cra 15 #22-10",
                         "observations":"Tratamiento",
                         "items":[{"cupsCode":"CUPS-1","description":"Consulta","quantity":1,"unitPrice":60000.0,"discountPercent":0.0}]}
                        """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.billNumber").value("FE-2026-0492"))
                .andExpect(jsonPath("$.totalCost").value(266000.0));
    }

    @Test
    @WithMockUser(roles = "Recepcionista")
    void getBillByIdReturnsBill() throws Exception {
        UUID id = UUID.randomUUID();
        when(billService.get(eq(id))).thenReturn(
                new BillResponse(id, "FE-2026-0492", Instant.now(), 100L, "Maria Garcia", 100L,
                        "Cra 15 #22-10", "Tarjeta de Credito", 280000.0, 14000.0, 0.0, 266000.0,
                        "DOSCIENTOS SESENTA Y SEIS MIL", "cufe-abc", "Res. DIAN", "qr-data",
                        "Tratamiento", Collections.emptyList())
        );

        mvc.perform(get("/api/bills/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.billNumber").value("FE-2026-0492"));
    }
}

