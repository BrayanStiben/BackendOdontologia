package co.edu.uniquindio.AgendasDT.service;

import co.edu.uniquindio.AgendasDT.domain.DentalChair;
import co.edu.uniquindio.AgendasDT.domain.User;
import co.edu.uniquindio.AgendasDT.domain.enums.UserRol;
import co.edu.uniquindio.AgendasDT.dto.PublicBookingRequest;
import co.edu.uniquindio.AgendasDT.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.*;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PublicBookingServiceTests {
    @Mock MeetingRepository meetings;
    @Mock UserRepository users;
    @Mock DentalChairRepository chairs;
    @Mock MeetingAuditRepository audit;
    @Mock NotificationRepository notifications;

    Clock clock = Clock.fixed(Instant.parse("2026-09-09T08:00:00Z"), ZoneOffset.UTC);
    PublicBookingService service;

    @BeforeEach
    void setUp() {
        service = new PublicBookingService(meetings, users, chairs, audit, notifications, clock);
    }

    @Test
    void bookCreatesMeetingAndNotification() {
        User doctor = new User();
        doctor.setId(10L);
        doctor.setName("Alex");
        doctor.setLastName("Gonzalez");
        doctor.setRol(UserRol.Odontologo);

        DentalChair chair = new DentalChair();
        chair.setId(1L);
        chair.setName("Consultorio 20");
        chair.setActive(true);

        when(users.findById(10L)).thenReturn(Optional.of(doctor));
        when(users.findById(12345L)).thenReturn(Optional.empty());
        when(users.saveAndFlush(any())).thenAnswer(inv -> inv.getArgument(0));
        when(chairs.findAll()).thenReturn(List.of(chair));
        when(meetings.overlaps(any(), any(), any(), any(), any(), any(), any())).thenReturn(false);
        when(meetings.saveAndFlush(any())).thenAnswer(inv -> inv.getArgument(0));

        var req = new PublicBookingRequest(
                10L,
                12345L,
                "Carlos",
                "Gomez",
                "+573001112233",
                "carlos@example.com",
                OffsetDateTime.parse("2026-09-10T10:00:00-05:00"),
                OffsetDateTime.parse("2026-09-10T10:45:00-05:00"),
                "Valoracion",
                true
        );

        var res = service.book(req);

        assertNotNull(res);
        assertEquals("Dr. Gonzalez", res.doctorName());
        assertEquals("Carlos Gomez", res.patientName());
        assertEquals("Consultorio 20", res.chairName());
        verify(meetings).saveAndFlush(any());
        verify(audit).save(any());
        verify(notifications).save(any());
    }
}
