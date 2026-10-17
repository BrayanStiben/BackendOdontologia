package co.edu.uniquindio.AgendasDT.controller;

import co.edu.uniquindio.AgendasDT.domain.Medicine;
import co.edu.uniquindio.AgendasDT.domain.enums.MeetingStatus;
import co.edu.uniquindio.AgendasDT.dto.DashboardSummaryResponse;
import co.edu.uniquindio.AgendasDT.dto.RestockRequest;
import co.edu.uniquindio.AgendasDT.service.DashboardService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/dashboard")
@PreAuthorize("hasAnyRole('Administrador', 'Recepcionista', 'Odontologo')")
public class DashboardController {
    private final DashboardService service;

    public DashboardController(DashboardService service) {
        this.service = service;
    }

    @GetMapping("/summary")
    public DashboardSummaryResponse getSummary() {
        return service.getSummary();
    }

    @PutMapping("/meetings/{id}/status")
    public ResponseEntity<Map<String, String>> updateMeetingStatus(
            @PathVariable UUID id,
            @RequestParam MeetingStatus status) {
        service.updateMeetingStatus(id, status);
        return ResponseEntity.ok(Map.of("message", "Estado de la cita actualizado a " + status));
    }

    @PostMapping("/restock")
    public ResponseEntity<Medicine> restock(@Valid @RequestBody RestockRequest request) {
        return ResponseEntity.ok(service.restock(request.medicineId(), request.quantity()));
    }
}

