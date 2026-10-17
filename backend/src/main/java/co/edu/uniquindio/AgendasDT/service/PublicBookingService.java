package co.edu.uniquindio.AgendasDT.service;

import co.edu.uniquindio.AgendasDT.domain.*;
import co.edu.uniquindio.AgendasDT.domain.enums.MeetingStatus;
import co.edu.uniquindio.AgendasDT.domain.enums.NotificationStatus;
import co.edu.uniquindio.AgendasDT.domain.enums.UserRol;
import co.edu.uniquindio.AgendasDT.dto.*;
import co.edu.uniquindio.AgendasDT.exception.InvalidMeetingException;
import co.edu.uniquindio.AgendasDT.exception.MeetingConflictException;
import co.edu.uniquindio.AgendasDT.exception.MeetingNotFoundException;
import co.edu.uniquindio.AgendasDT.repository.*;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@Validated
@Transactional
public class PublicBookingService {
    private static final List<MeetingStatus> RESERVED = List.of(MeetingStatus.Pendiente, MeetingStatus.Confirmada);
    private static final UUID NO_MEETING = new UUID(0, 0);
    private static final ZoneId CLINIC_ZONE = ZoneId.of("America/Bogota");
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("hh:mm a");

    private final MeetingRepository meetings;
    private final UserRepository users;
    private final DentalChairRepository chairs;
    private final MeetingAuditRepository audit;
    private final NotificationRepository notifications;
    private final Clock clock;

    public PublicBookingService(MeetingRepository meetings, UserRepository users, DentalChairRepository chairs,
                                MeetingAuditRepository audit, NotificationRepository notifications, Clock clock) {
        this.meetings = meetings;
        this.users = users;
        this.chairs = chairs;
        this.audit = audit;
        this.notifications = notifications;
        this.clock = clock;
    }

    @Transactional(readOnly = true)
    public List<String> getSpecialties() {
        List<String> found = users.findByRol(UserRol.Odontologo).stream()
                .map(User::getSpecialty)
                .filter(Objects::nonNull)
                .filter(s -> !s.isBlank())
                .distinct()
                .toList();
        if (found.isEmpty()) {
            return List.of("Odontología General", "Ortodoncia", "Endodoncia", "Periodoncia", "Cirugía Oral");
        }
        return found;
    }

    @Transactional(readOnly = true)
    public List<DoctorSummaryResponse> getDoctors(String specialty) {
        List<User> list = (specialty != null && !specialty.isBlank())
                ? users.findByRolAndSpecialty(UserRol.Odontologo, specialty)
                : users.findByRol(UserRol.Odontologo);

        return list.stream()
                .filter(User::isActive)
                .map(u -> new DoctorSummaryResponse(u.getId(), u.getName(), u.getLastName(), u.getSpecialty() != null ? u.getSpecialty() : "Odontología General"))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<PublicSlotResponse> getAvailableSlots(Long doctorId, LocalDate date) {
        users.findById(doctorId)
                .filter(u -> u.getRol() == UserRol.Odontologo)
                .orElseThrow(() -> new MeetingNotFoundException("El odontólogo no existe"));

        List<DentalChair> activeChairs = chairs.findAll().stream().filter(DentalChair::isActive).toList();
        if (activeChairs.isEmpty()) {
            return Collections.emptyList();
        }

        List<PublicSlotResponse> slots = new ArrayList<>();
        int[] hours = {8, 9, 10, 11, 14, 15, 16};

        for (int h : hours) {
            ZonedDateTime startZdt = date.atTime(h, 0).atZone(CLINIC_ZONE);
            ZonedDateTime endZdt = startZdt.plusMinutes(45);
            Instant start = startZdt.toInstant();
            Instant end = endZdt.toInstant();

            if (!start.isAfter(clock.instant())) {
                continue;
            }

            boolean anyChairFree = activeChairs.stream().anyMatch(chair ->
                    !meetings.overlaps(NO_MEETING, RESERVED, start, end, 0L, doctorId, chair.getId())
            );

            if (anyChairFree) {
                slots.add(new PublicSlotResponse(start, end, startZdt.format(TIME_FORMAT)));
            }
        }
        return slots;
    }

    public PublicBookingResponse book(@Valid PublicBookingRequest request) {
        Instant start = request.startsAt().toInstant();
        Instant end = request.endsAt().toInstant();
        if (!start.isAfter(clock.instant())) {
            throw new InvalidMeetingException("La cita debe iniciar en el futuro.");
        }
        if (!end.isAfter(start)) {
            throw new InvalidMeetingException("La hora de fin debe ser posterior al inicio.");
        }

        User doctor = users.findById(request.doctorId())
                .filter(u -> u.getRol() == UserRol.Odontologo)
                .orElseThrow(() -> new MeetingNotFoundException("El odontólogo indicado no existe."));

        String email = request.patientEmail().strip().toLowerCase(Locale.ROOT);
        User patient = users.findById(request.patientId()).orElseGet(() -> {
            User nuevo = new User();
            nuevo.setId(request.patientId());
            nuevo.setName(request.patientName().strip());
            nuevo.setLastName(request.patientLastName().strip());
            nuevo.setEmail(email);
            nuevo.setPhone(request.patientPhone().strip());
            nuevo.setRol(UserRol.Paciente);
            nuevo.setBirthDate(LocalDate.of(2000, 1, 1));
            return users.saveAndFlush(nuevo);
        });

        DentalChair assignedChair = chairs.findAll().stream()
                .filter(DentalChair::isActive)
                .filter(c -> !meetings.overlaps(NO_MEETING, RESERVED, start, end, patient.getId(), doctor.getId(), c.getId()))
                .findFirst()
                .orElseThrow(() -> new MeetingConflictException("No hay disponibilidad de sillón u odontólogo para este horario."));

        Meeting meeting = new Meeting();
        meeting.setId(UUID.randomUUID());
        meeting.setPatient(patient);
        meeting.setDoctor(doctor);
        meeting.setChair(assignedChair);
        meeting.setStartsAt(start);
        meeting.setEndsAt(end);
        meeting.setReason(request.reason() != null && !request.reason().isBlank() ? request.reason().strip() : "Reserva en línea 24/7");
        meeting.setStatus(MeetingStatus.Pendiente);

        meetings.saveAndFlush(meeting);
        audit.save(new MeetingAudit(meeting.getId(), "PUBLIC_BOOKING_24_7", "CREATE", clock.instant()));

        String type = Boolean.TRUE.equals(request.receiveWhatsAppNotification()) ? "WHATSAPP" : "EMAIL";
        String recipient = "WHATSAPP".equals(type) ? patient.getPhone() : patient.getEmail();
        String message = String.format("Hola %s, tu cita odontológica con Dr. %s fue agendada para %s. Código: %s",
                patient.getName(), doctor.getLastName(), request.startsAt(), meeting.getId());

        notifications.save(new Notification(meeting, type, recipient, message, clock.instant(), NotificationStatus.Enviada));

        return new PublicBookingResponse(
                meeting.getId(),
                meeting.getId().toString(),
                start,
                end,
                "Dr. " + doctor.getLastName(),
                patient.getName() + " " + patient.getLastName(),
                assignedChair.getName(),
                meeting.getStatus(),
                "Cita reservada con éxito. Código de confirmación: " + meeting.getId()
        );
    }
}
