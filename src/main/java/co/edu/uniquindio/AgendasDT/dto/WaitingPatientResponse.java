package co.edu.uniquindio.AgendasDT.dto;

import co.edu.uniquindio.AgendasDT.domain.enums.MeetingStatus;
import java.time.Instant;
import java.util.UUID;

public record WaitingPatientResponse(
        UUID meetingId,
        Instant startsAt,
        String timeFormatted,
        Long patientId,
        String patientName,
        Long doctorId,
        String doctorName,
        String chairName,
        MeetingStatus status
) { }

