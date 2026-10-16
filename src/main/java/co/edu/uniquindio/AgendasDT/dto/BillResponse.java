package co.edu.uniquindio.AgendasDT.dto;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record BillResponse(
        UUID id,
        String billNumber,
        Instant issueDate,
        Long patientId,
        String patientName,
        Long patientDocument,
        String address,
        String paymentMethod,
        Double subtotal,
        Double discount,
        Double tax,
        Double totalCost,
        String totalInWords,
        String cufe,
        String dianResolution,
        String qrData,
        String observations,
        List<BillItemResponse> items
) { }

