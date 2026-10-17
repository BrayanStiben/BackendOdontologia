package co.edu.uniquindio.AgendasDT.dto;

import co.edu.uniquindio.AgendasDT.domain.Meeting;
import co.edu.uniquindio.AgendasDT.domain.enums.MeetingStatus;
import java.time.Instant;
import java.util.UUID;

public record MeetingResponse(UUID id, String confirmationCode, Long patientId, Long doctorId, Long chairId,
                              Instant startsAt, Instant endsAt, String reason, MeetingStatus status, Long version) {
    public static MeetingResponse from(Meeting meeting) {
        return new MeetingResponse(meeting.getId(), meeting.getId().toString(), meeting.getPatient().getId(),
                meeting.getDoctor().getId(), meeting.getChair().getId(), meeting.getStartsAt(),
                meeting.getEndsAt(), meeting.getReason(), meeting.getStatus(), meeting.getVersion());
    }
}
