package co.edu.uniquindio.AgendasDT.dto;

import java.time.LocalDate;
import java.util.List;

public record PatientClinicalProfileResponse(
        Long patientId,
        String name,
        String lastName,
        LocalDate birthDate,
        String phone,
        String email,
        List<ClinicalHistoryResponse> historyRecords
) { }

