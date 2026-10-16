package co.edu.uniquindio.AgendasDT.dto;

import java.util.UUID;

public record LowStockAlertResponse(
        UUID medicineId,
        String medicineName,
        Integer stock,
        Integer minimumStock,
        Double unitCost
) { }

