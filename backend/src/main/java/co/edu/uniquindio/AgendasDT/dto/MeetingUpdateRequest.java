package co.edu.uniquindio.AgendasDT.dto;

import jakarta.validation.constraints.*;
import java.time.OffsetDateTime;

public record MeetingUpdateRequest(
        @NotNull @Positive Long doctorId,
        @NotNull @Positive Long chairId,
        @NotNull OffsetDateTime startsAt,
        @NotNull OffsetDateTime endsAt,
        @NotBlank @Size(max = 500) String reason,
        @NotNull @PositiveOrZero Long version) { }
