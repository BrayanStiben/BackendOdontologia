package co.edu.uniquindio.AgendasDT.dto;

import co.edu.uniquindio.AgendasDT.domain.enums.MeetingStatus;
import java.time.Instant;
import java.util.UUID;

public record PublicBookingResponse(
        UUID meetingId,
        String confirmationCode,
        Instant startsAt,
        Instant endsAt,
        String doctorName,
        String patientName,
        String chairName,
        MeetingStatus status,
        String message
) { }

