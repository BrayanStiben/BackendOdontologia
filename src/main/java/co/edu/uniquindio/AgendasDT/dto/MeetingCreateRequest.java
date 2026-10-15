package co.edu.uniquindio.AgendasDT.dto;

import jakarta.validation.constraints.*;
import java.time.OffsetDateTime;

public record MeetingCreateRequest(
        @NotNull @Positive Long patientId,
        @NotNull @Positive Long doctorId,
        @NotNull @Positive Long chairId,
        @NotNull OffsetDateTime startsAt,
        @NotNull OffsetDateTime endsAt,
        @NotBlank @Size(max = 500) String reason) { }
