package co.edu.uniquindio.AgendasDT.service;

import co.edu.uniquindio.AgendasDT.domain.ClinicalHistory;
import co.edu.uniquindio.AgendasDT.domain.User;
import co.edu.uniquindio.AgendasDT.domain.enums.UserRol;
import co.edu.uniquindio.AgendasDT.dto.ClinicalEvaluationRequest;
import co.edu.uniquindio.AgendasDT.dto.ClinicalEvolutionRequest;
import co.edu.uniquindio.AgendasDT.repository.ClinicalHistoryRepository;
import co.edu.uniquindio.AgendasDT.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClinicalHistoryServiceTests {
    @Mock ClinicalHistoryRepository historyRepo;
    @Mock UserRepository users;

    Clock clock = Clock.fixed(Instant.parse("2026-09-09T09:00:00Z"), ZoneOffset.UTC);
    ClinicalHistoryService service;

    @BeforeEach
    void setUp() {
        service = new ClinicalHistoryService(historyRepo, users, clock);
    }

    @Test
    void addEvolutionSavesCorrectRecord() {
        User patient = new User();
        patient.setId(100L);
        patient.setRol(UserRol.Paciente);

        User doctor = new User();
        doctor.setId(200L);
        doctor.setLastName("Gonzalez");
        doctor.setRol(UserRol.Odontologo);

        when(users.findById(100L)).thenReturn(Optional.of(patient));
        when(users.findById(200L)).thenReturn(Optional.of(doctor));

        var req = new ClinicalEvolutionRequest(
                100L,
                200L,
                "#16 (Molar)",
                "Resina de Fotocurado",
                "Caries oclusal",
                "Cavidad clase I"
        );

        var res = service.addEvolution(req);

        assertNotNull(res);
        assertEquals("#16 (Molar)", res.toothPiece());
        assertEquals("Resina de Fotocurado", res.procedureName());
        assertEquals("Dr. Gonzalez", res.doctorName());
        assertTrue(res.signed());
        verify(historyRepo).save(any(ClinicalHistory.class));
    }

    @Test
    void addEvaluationSavesFullAnamnesis() {
        User patient = new User();
        patient.setId(100L);
        patient.setRol(UserRol.Paciente);

        User doctor = new User();
        doctor.setId(200L);
        doctor.setLastName("Gonzalez");
        doctor.setRol(UserRol.Odontologo);

        when(users.findById(100L)).thenReturn(Optional.of(patient));
        when(users.findById(200L)).thenReturn(Optional.of(doctor));

        var req = new ClinicalEvaluationRequest(
                100L,
                200L,
                "Dolor al masticar",
                "Molestia desde hace 3 dias",
                "120/80",
                "72 bpm",
                "36.5 C",
                "62 kg",
                "Normal",
                "Gingivitis marginal",
                "Regular",
                "28%",
                true,
                false,
                "K02.1 - Caries dentina",
                "K05.1 - Gingivitis",
                "Favorable",
                "Detartraje y resina"
        );

        var res = service.addEvaluation(req);

        assertNotNull(res);
        assertEquals("K02.1 - Caries dentina", res.diagnosis());
        verify(historyRepo).save(any(ClinicalHistory.class));
    }
}

