package co.edu.uniquindio.AgendasDT.service;

import co.edu.uniquindio.AgendasDT.domain.Meeting;
import co.edu.uniquindio.AgendasDT.domain.Medicine;
import co.edu.uniquindio.AgendasDT.domain.enums.MeetingStatus;
import co.edu.uniquindio.AgendasDT.dto.DashboardSummaryResponse;
import co.edu.uniquindio.AgendasDT.dto.LowStockAlertResponse;
import co.edu.uniquindio.AgendasDT.dto.WaitingPatientResponse;
import co.edu.uniquindio.AgendasDT.exception.MeetingNotFoundException;
import co.edu.uniquindio.AgendasDT.repository.BillRepository;
import co.edu.uniquindio.AgendasDT.repository.MedicineRepository;
import co.edu.uniquindio.AgendasDT.repository.MeetingRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@PreAuthorize("hasAnyRole('Administrador', 'Recepcionista', 'Odontologo')")
public class DashboardService {
    private final MeetingRepository meetings;
    private final BillRepository bills;
    private final MedicineRepository medicines;
    private final Clock clock;
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("hh:mm a");

    public DashboardService(MeetingRepository meetings, BillRepository bills, MedicineRepository medicines, Clock clock) {
        this.meetings = meetings;
        this.bills = bills;
        this.medicines = medicines;
        this.clock = clock;
    }

    @Transactional(readOnly = true)
    public DashboardSummaryResponse getSummary() {
        LocalDate today = LocalDate.now(clock);
        Instant startOfDay = today.atStartOfDay(ZoneOffset.UTC).toInstant();
        Instant endOfDay = today.plusDays(1).atStartOfDay(ZoneOffset.UTC).toInstant();

        long citasHoy = meetings.countByStartsAtBetween(startOfDay, endOfDay);
        long atendidos = meetings.countByStartsAtBetweenAndStatus(startOfDay, endOfDay, MeetingStatus.Completada);
        Double ingresos = bills.sumTotalBetween(startOfDay, endOfDay);
        long stockCritico = medicines.countLowStock();

        List<WaitingPatientResponse> proximos = meetings.findByStartsAtBetweenOrderByStartsAtAsc(startOfDay, endOfDay)
                .stream()
                .map(this::mapToWaitingPatient)
                .toList();

        List<LowStockAlertResponse> alertas = medicines.findLowStock()
                .stream()
                .map(m -> new LowStockAlertResponse(m.getId(), m.getMedicineName(), m.getStock(), m.getMinimumStock(), m.getUnitCost()))
                .toList();

        return new DashboardSummaryResponse(citasHoy, atendidos, ingresos, stockCritico, proximos, alertas);
    }

    public void updateMeetingStatus(UUID meetingId, MeetingStatus status) {
        Meeting meeting = meetings.findById(meetingId)
                .orElseThrow(() -> new MeetingNotFoundException("La cita no existe."));
        meeting.setStatus(status);
        meetings.save(meeting);
    }

    public Medicine restock(UUID medicineId, int quantity) {
        Medicine medicine = medicines.findById(medicineId)
                .orElseThrow(() -> new IllegalArgumentException("Medicamento no encontrado"));
        medicine.setStock(medicine.getStock() + quantity);
        return medicines.save(medicine);
    }

    private WaitingPatientResponse mapToWaitingPatient(Meeting m) {
        ZonedDateTime zdt = m.getStartsAt().atZone(ZoneId.of("America/Bogota"));
        String timeStr = zdt.format(TIME_FORMATTER);
        String patientName = m.getPatient().getName() + " " + m.getPatient().getLastName();
        String doctorName = "Dr. " + m.getDoctor().getLastName();
        String chairName = m.getChair() != null ? m.getChair().getName() : "Sillón no asignado";
        return new WaitingPatientResponse(
                m.getId(),
                m.getStartsAt(),
                timeStr,
                m.getPatient().getId(),
                patientName,
                m.getDoctor().getId(),
                doctorName,
                chairName,
                m.getStatus()
        );
    }
}

