package co.edu.uniquindio.AgendasDT.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.UUID;

public record RestockRequest(
        @NotNull UUID medicineId,
        @NotNull @Positive Integer quantity
) { }

