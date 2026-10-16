package co.edu.uniquindio.AgendasDT.dto;

import java.util.List;

public record DashboardSummaryResponse(
        long citasParaHoy,
        long pacientesAtendidos,
        Double ingresosEstimados,
        long stockCritico,
        List<WaitingPatientResponse> proximosPacientes,
        List<LowStockAlertResponse> alertasStock
) { }

