package co.edu.uniquindio.AgendasDT.controller;

import co.edu.uniquindio.AgendasDT.dto.ClinicalEvaluationRequest;
import co.edu.uniquindio.AgendasDT.dto.ClinicalEvolutionRequest;
import co.edu.uniquindio.AgendasDT.dto.ClinicalHistoryResponse;
import co.edu.uniquindio.AgendasDT.dto.PatientClinicalProfileResponse;
import co.edu.uniquindio.AgendasDT.service.ClinicalHistoryService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/clinical-histories")
@PreAuthorize("hasAnyRole('Administrador', 'Odontologo')")
public class ClinicalHistoryController {
    private final ClinicalHistoryService historyService;

    public ClinicalHistoryController(ClinicalHistoryService historyService) {
        this.historyService = historyService;
    }

    @GetMapping("/patient/{patientId}")
    public PatientClinicalProfileResponse getPatientProfile(@PathVariable Long patientId) {
        return historyService.getPatientProfile(patientId);
    }

    @PostMapping("/evolution")
    public ResponseEntity<ClinicalHistoryResponse> addEvolution(@Valid @RequestBody ClinicalEvolutionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(historyService.addEvolution(request));
    }

    @PostMapping("/evaluation")
    public ResponseEntity<ClinicalHistoryResponse> addEvaluation(@Valid @RequestBody ClinicalEvaluationRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(historyService.addEvaluation(request));
    }
}

