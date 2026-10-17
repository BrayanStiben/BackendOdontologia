package co.edu.uniquindio.AgendasDT.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public record BillItemRequest(
        String cupsCode,
        @NotBlank String description,
        @NotNull @Positive Integer quantity,
        @NotNull @PositiveOrZero Double unitPrice,
        @PositiveOrZero Double discountPercent
) { }

