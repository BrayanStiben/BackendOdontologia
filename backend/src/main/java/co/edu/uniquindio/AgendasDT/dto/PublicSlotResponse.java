package co.edu.uniquindio.AgendasDT.dto;

import java.time.Instant;

public record PublicSlotResponse(
        Instant startsAt,
        Instant endsAt,
        String timeFormatted
) { }

