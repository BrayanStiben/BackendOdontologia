package co.edu.uniquindio.AgendasDT.dto;

public record BillItemResponse(
        int itemNumber,
        String code,
        String description,
        int quantity,
        Double unitPrice,
        Double discount,
        Double total
) { }

