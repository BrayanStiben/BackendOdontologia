package co.edu.uniquindio.AgendasDT.dto;

import java.time.Instant;
import java.util.UUID;

public record ClinicalHistoryResponse(
        UUID id,
        Instant recordDate,
        String toothPiece,
        String procedureName,
        String diagnosis,
        String observations,
        String doctorName,
        boolean signed
) { }

