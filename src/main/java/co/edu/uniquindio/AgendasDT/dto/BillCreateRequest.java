package co.edu.uniquindio.AgendasDT.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.List;

public record BillCreateRequest(
        @NotNull @Positive Long patientId,
        String paymentMethod,
        String address,
        String observations,
        @NotEmpty @Valid List<BillItemRequest> items
) { }

