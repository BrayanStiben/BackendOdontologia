package co.edu.uniquindio.AgendasDT.service;

import co.edu.uniquindio.AgendasDT.domain.DentalChair;
import co.edu.uniquindio.AgendasDT.domain.Meeting;
import co.edu.uniquindio.AgendasDT.domain.Medicine;
import co.edu.uniquindio.AgendasDT.domain.User;
import co.edu.uniquindio.AgendasDT.domain.enums.MeetingStatus;
import co.edu.uniquindio.AgendasDT.repository.BillRepository;
import co.edu.uniquindio.AgendasDT.repository.MedicineRepository;
import co.edu.uniquindio.AgendasDT.repository.MeetingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DashboardServiceTests {
    @Mock MeetingRepository meetings;
    @Mock BillRepository bills;
    @Mock MedicineRepository medicines;

    Clock clock = Clock.fixed(Instant.parse("2026-09-09T10:00:00Z"), ZoneOffset.UTC);
    DashboardService service;

    @BeforeEach
    void setUp() {
        service = new DashboardService(meetings, bills, medicines, clock);
    }

    @Test
    void getSummaryComputesCorrectMetrics() {
        when(meetings.countByStartsAtBetween(any(), any())).thenReturn(12L);
        when(meetings.countByStartsAtBetweenAndStatus(any(), any(), eq(MeetingStatus.Completada))).thenReturn(8L);
        when(bills.sumTotalBetween(any(), any())).thenReturn(680.0);
        when(medicines.countLowStock()).thenReturn(1L);

        User patient = new User();
        patient.setId(1L);
        patient.setName("Maria");
        patient.setLastName("Garcia");

        User doctor = new User();
        doctor.setId(2L);
        doctor.setName("Alex");
        doctor.setLastName("Gonzalez");

        DentalChair chair = new DentalChair();
        chair.setName("Consultorio 20");

        Meeting meeting = new Meeting();
        meeting.setId(UUID.randomUUID());
        meeting.setPatient(patient);
        meeting.setDoctor(doctor);
        meeting.setChair(chair);
        meeting.setStartsAt(Instant.parse("2026-09-09T13:30:00Z"));
        meeting.setStatus(MeetingStatus.Pendiente);

        when(meetings.findByStartsAtBetweenOrderByStartsAtAsc(any(), any())).thenReturn(List.of(meeting));

        Medicine low = new Medicine("Resina Z350", "Desc", 2, 5, 45000.0);
        low.setId(UUID.randomUUID());
        when(medicines.findLowStock()).thenReturn(List.of(low));

        var summary = service.getSummary();

        assertNotNull(summary);
        assertEquals(12L, summary.citasParaHoy());
        assertEquals(8L, summary.pacientesAtendidos());
        assertEquals(680.0, summary.ingresosEstimados());
        assertEquals(1L, summary.stockCritico());
        assertEquals(1, summary.proximosPacientes().size());
        assertEquals("Maria Garcia", summary.proximosPacientes().get(0).patientName());
        assertEquals(1, summary.alertasStock().size());
        assertEquals("Resina Z350", summary.alertasStock().get(0).medicineName());
    }

    @Test
    void restockIncreasesMedicineStock() {
        UUID id = UUID.randomUUID();
        Medicine m = new Medicine("Guantes", "Caja", 3, 5, 10000.0);
        m.setId(id);

        when(medicines.findById(id)).thenReturn(Optional.of(m));
        when(medicines.save(any())).thenAnswer(inv -> inv.getArgument(0));

        var updated = service.restock(id, 10);
        assertEquals(13, updated.getStock());
    }
}

