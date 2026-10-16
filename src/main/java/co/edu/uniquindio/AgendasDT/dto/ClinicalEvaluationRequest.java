package co.edu.uniquindio.AgendasDT.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record ClinicalEvaluationRequest(
        @NotNull @Positive Long patientId,
        @NotNull @Positive Long doctorId,
        @NotBlank @Size(max = 500) String motivoConsulta,
        @NotBlank @Size(max = 1000) String enfermedadActual,
        String bloodPressure,
        String heartRate,
        String temperature,
        String weight,
        String atmArticulation,
        String softTissues,
        String oralHygiene,
        String plaqueIndex,
        Boolean dentalCalculus,
        Boolean foodImpaction,
        @NotBlank @Size(max = 250) String primaryDiagnosis,
        String secondaryDiagnosis,
        String prognosis,
        String suggestedPlan
) { }

