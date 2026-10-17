package co.edu.uniquindio.AgendasDT.controller;

import co.edu.uniquindio.AgendasDT.dto.DoctorSummaryResponse;
import co.edu.uniquindio.AgendasDT.dto.PublicBookingRequest;
import co.edu.uniquindio.AgendasDT.dto.PublicBookingResponse;
import co.edu.uniquindio.AgendasDT.dto.PublicSlotResponse;
import co.edu.uniquindio.AgendasDT.service.PublicBookingService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/public")
public class PublicBookingController {
    private final PublicBookingService bookingService;

    public PublicBookingController(PublicBookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping("/specialties")
    public List<String> getSpecialties() {
        return bookingService.getSpecialties();
    }

    @GetMapping("/doctors")
    public List<DoctorSummaryResponse> getDoctors(@RequestParam(required = false) String specialty) {
        return bookingService.getDoctors(specialty);
    }

    @GetMapping("/slots")
    public List<PublicSlotResponse> getSlots(
            @RequestParam Long doctorId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return bookingService.getAvailableSlots(doctorId, date);
    }

    @PostMapping("/book")
    public ResponseEntity<PublicBookingResponse> book(@Valid @RequestBody PublicBookingRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookingService.book(request));
    }
}

