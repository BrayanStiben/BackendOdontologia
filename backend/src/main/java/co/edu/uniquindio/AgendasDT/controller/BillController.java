package co.edu.uniquindio.AgendasDT.controller;

import co.edu.uniquindio.AgendasDT.dto.BillCreateRequest;
import co.edu.uniquindio.AgendasDT.dto.BillResponse;
import co.edu.uniquindio.AgendasDT.service.BillService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/bills")
@PreAuthorize("hasAnyRole('Administrador', 'Recepcionista')")
public class BillController {
    private final BillService billService;

    public BillController(BillService billService) {
        this.billService = billService;
    }

    @PostMapping
    public ResponseEntity<BillResponse> create(@Valid @RequestBody BillCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(billService.create(request));
    }

    @GetMapping("/{id}")
    public BillResponse get(@PathVariable UUID id) {
        return billService.get(id);
    }

    @GetMapping("/patient/{patientId}")
    public List<BillResponse> getByPatient(@PathVariable Long patientId) {
        return billService.getByPatient(patientId);
    }
}

