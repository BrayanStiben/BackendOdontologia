package co.edu.uniquindio.AgendasDT.controller;

import co.edu.uniquindio.AgendasDT.config.MethodSecurityConfig;
import co.edu.uniquindio.AgendasDT.domain.enums.MeetingStatus;
import co.edu.uniquindio.AgendasDT.dto.DoctorSummaryResponse;
import co.edu.uniquindio.AgendasDT.dto.PublicBookingResponse;
import co.edu.uniquindio.AgendasDT.dto.PublicSlotResponse;
import co.edu.uniquindio.AgendasDT.exception.ApiExceptionHandler;
import co.edu.uniquindio.AgendasDT.service.PublicBookingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PublicBookingController.class)
@Import({co.edu.uniquindio.AgendasDT.config.SecurityConfig.class, MethodSecurityConfig.class, ApiExceptionHandler.class})
class PublicBookingControllerTests {
    @Autowired MockMvc mvc;
    @MockitoBean PublicBookingService bookingService;

    @Test
    void getSpecialtiesReturnsList() throws Exception {
        when(bookingService.getSpecialties()).thenReturn(List.of("Odontología General", "Ortodoncia"));
        mvc.perform(get("/api/public/specialties"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value("Odontología General"));
    }

    @Test
    void getDoctorsReturnsList() throws Exception {
        when(bookingService.getDoctors(any())).thenReturn(List.of(
                new DoctorSummaryResponse(1L, "Alex", "Gonzalez", "Odontología General")
        ));
        mvc.perform(get("/api/public/doctors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Alex"));
    }

    @Test
    void getSlotsReturnsSlots() throws Exception {
        Instant now = Instant.parse("2026-09-10T14:00:00Z");
        when(bookingService.getAvailableSlots(eq(1L), any())).thenReturn(List.of(
                new PublicSlotResponse(now, now.plusSeconds(2700), "09:00 AM")
        ));
        mvc.perform(get("/api/public/slots").param("doctorId", "1").param("date", "2026-09-10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].timeFormatted").value("09:00 AM"));
    }

    @Test
    void bookCreatesAppointment() throws Exception {
        UUID id = UUID.randomUUID();
        when(bookingService.book(any())).thenReturn(
                new PublicBookingResponse(id, id.toString(), Instant.now(), Instant.now().plusSeconds(2700),
                        "Dr. Gonzalez", "Carlos Gomez", "Sillón 1", MeetingStatus.Pendiente, "Exito")
        );

        mvc.perform(post("/api/public/book").with(csrf())
                        .contentType("application/json")
                        .content("""
                        {"doctorId":1,"patientId":123,"patientName":"Carlos","patientLastName":"Gomez",
                         "patientPhone":"+573001234567","patientEmail":"carlos@doc.com",
                         "startsAt":"2026-09-10T09:00:00-05:00","endsAt":"2026-09-10T09:45:00-05:00",
                         "reason":"Control","receiveWhatsAppNotification":true}
                        """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.doctorName").value("Dr. Gonzalez"))
                .andExpect(jsonPath("$.status").value("Pendiente"));
    }
}
