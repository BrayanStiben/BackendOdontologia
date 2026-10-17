package co.edu.uniquindio.AgendasDT.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record ClinicalEvolutionRequest(
        @NotNull @Positive Long patientId,
        @NotNull @Positive Long doctorId,
        @NotBlank @Size(max = 50) String toothPiece,
        @NotBlank @Size(max = 200) String procedureName,
        @NotBlank @Size(max = 1000) String diagnosis,
        @Size(max = 2000) String observations
) { }

