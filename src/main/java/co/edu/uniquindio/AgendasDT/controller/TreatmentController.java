package co.edu.uniquindio.AgendasDT.controller;

import co.edu.uniquindio.AgendasDT.domain.Treatment;
import co.edu.uniquindio.AgendasDT.repository.TreatmentRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/treatments")
@PreAuthorize("hasAnyRole('Administrador', 'Recepcionista', 'Odontologo')")
public class TreatmentController {
    private final TreatmentRepository repository;

    public TreatmentController(TreatmentRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Treatment> getAll() {
        return repository.findAll();
    }

    @PostMapping
    public ResponseEntity<Treatment> create(@RequestBody Treatment treatment) {
        return ResponseEntity.status(HttpStatus.CREATED).body(repository.save(treatment));
    }
}

