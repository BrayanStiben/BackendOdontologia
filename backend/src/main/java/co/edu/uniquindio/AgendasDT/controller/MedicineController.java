package co.edu.uniquindio.AgendasDT.controller;

import co.edu.uniquindio.AgendasDT.domain.Medicine;
import co.edu.uniquindio.AgendasDT.repository.MedicineRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/medicines")
@PreAuthorize("hasAnyRole('Administrador', 'Recepcionista', 'Odontologo')")
public class MedicineController {
    private final MedicineRepository repository;

    public MedicineController(MedicineRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Medicine> getAll() {
        return repository.findAll();
    }

    @GetMapping("/low-stock")
    public List<Medicine> getLowStock() {
        return repository.findLowStock();
    }

    @PostMapping
    public ResponseEntity<Medicine> create(@RequestBody Medicine medicine) {
        return ResponseEntity.status(HttpStatus.CREATED).body(repository.save(medicine));
    }

    @PutMapping("/{id}")
    public Medicine update(@PathVariable UUID id, @RequestBody Medicine update) {
        Medicine existing = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Medicamento no encontrado"));
        existing.setMedicineName(update.getMedicineName());
        existing.setDescription(update.getDescription());
        existing.setStock(update.getStock());
        existing.setMinimumStock(update.getMinimumStock());
        existing.setUnitCost(update.getUnitCost());
        return repository.save(existing);
    }
}

