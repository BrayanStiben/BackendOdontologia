package co.edu.uniquindio.AgendasDT.dto;

import jakarta.validation.constraints.*;
import java.time.OffsetDateTime;

public record PublicBookingRequest(
        @NotNull(message = "El ID del odontólogo es obligatorio")
        @Positive
        Long doctorId,

        @NotNull(message = "El documento del paciente es obligatorio")
        @Positive
        Long patientId,

        @NotBlank(message = "El nombre del paciente es obligatorio")
        @Size(max = 100)
        String patientName,

        @NotBlank(message = "El apellido del paciente es obligatorio")
        @Size(max = 100)
        String patientLastName,

        @NotBlank(message = "El teléfono / WhatsApp es obligatorio")
        @Pattern(regexp = "^\\+?[0-9]{7,15}$")
        String patientPhone,

        @NotBlank(message = "El correo es obligatorio")
        @Email
        String patientEmail,

        @NotNull(message = "La hora de inicio es obligatoria")
        OffsetDateTime startsAt,

        @NotNull(message = "La hora de fin es obligatoria")
        OffsetDateTime endsAt,

        @Size(max = 500)
        String reason,

        Boolean receiveWhatsAppNotification
) { }

