package co.edu.uniquindio.AgendasDT.controller;

import co.edu.uniquindio.AgendasDT.config.MethodSecurityConfig;
import co.edu.uniquindio.AgendasDT.dto.DashboardSummaryResponse;
import co.edu.uniquindio.AgendasDT.exception.ApiExceptionHandler;
import co.edu.uniquindio.AgendasDT.service.DashboardService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DashboardController.class)
@Import({MethodSecurityConfig.class, ApiExceptionHandler.class})
class DashboardControllerTests {
    @Autowired MockMvc mvc;
    @MockitoBean DashboardService dashboardService;

    @Test
    @WithMockUser(roles = "Administrador")
    void getSummaryReturnsDashboardData() throws Exception {
        when(dashboardService.getSummary()).thenReturn(
                new DashboardSummaryResponse(12, 8, 680.0, 1, Collections.emptyList(), Collections.emptyList())
        );

        mvc.perform(get("/api/dashboard/summary"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.citasParaHoy").value(12))
                .andExpect(jsonPath("$.pacientesAtendidos").value(8))
                .andExpect(jsonPath("$.ingresosEstimados").value(680.0));
    }
}

