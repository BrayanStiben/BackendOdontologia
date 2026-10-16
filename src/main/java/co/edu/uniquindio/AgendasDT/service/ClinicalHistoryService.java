package co.edu.uniquindio.AgendasDT.service;

import co.edu.uniquindio.AgendasDT.domain.ClinicalHistory;
import co.edu.uniquindio.AgendasDT.domain.User;
import co.edu.uniquindio.AgendasDT.domain.enums.UserRol;
import co.edu.uniquindio.AgendasDT.dto.*;
import co.edu.uniquindio.AgendasDT.exception.UserNotFoundException;
import co.edu.uniquindio.AgendasDT.repository.ClinicalHistoryRepository;
import co.edu.uniquindio.AgendasDT.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.time.Clock;
import java.util.List;

@Service
@Validated
@Transactional
@PreAuthorize("hasAnyRole('Administrador', 'Odontologo')")
public class ClinicalHistoryService {
    private final ClinicalHistoryRepository historyRepo;
    private final UserRepository users;
    private final Clock clock;

    public ClinicalHistoryService(ClinicalHistoryRepository historyRepo, UserRepository users, Clock clock) {
        this.historyRepo = historyRepo;
        this.users = users;
        this.clock = clock;
    }

    @Transactional(readOnly = true)
    public PatientClinicalProfileResponse getPatientProfile(Long patientId) {
        User patient = users.findById(patientId)
                .orElseThrow(() -> new UserNotFoundException("El paciente no existe"));

        List<ClinicalHistoryResponse> records = historyRepo.findByPatientIdOrderByRecordDateDesc(patientId)
                .stream()
                .map(this::mapToResponse)
                .toList();

        return new PatientClinicalProfileResponse(
                patient.getId(),
                patient.getName(),
                patient.getLastName(),
                patient.getBirthDate(),
                patient.getPhone(),
                patient.getEmail(),
                records
        );
    }

    public ClinicalHistoryResponse addEvolution(@Valid ClinicalEvolutionRequest req) {
        User patient = users.findById(req.patientId())
                .filter(u -> u.getRol() == UserRol.Paciente)
                .orElseThrow(() -> new UserNotFoundException("Paciente no encontrado"));

        User doctor = users.findById(req.doctorId())
                .filter(u -> u.getRol() == UserRol.Odontologo)
                .orElseThrow(() -> new UserNotFoundException("Odontólogo no encontrado"));

        ClinicalHistory history = new ClinicalHistory();
        history.setPatient(patient);
        history.setDoctor(doctor);
        history.setToothPiece(req.toothPiece().strip());
        history.setProcedureName(req.procedureName().strip());
        history.setDiagnosis(req.diagnosis().strip());
        history.setObservations(req.observations() != null ? req.observations().strip() : null);
        history.setRecordDate(clock.instant());
        history.setSigned(true);

        historyRepo.save(history);
        return mapToResponse(history);
    }

    public ClinicalHistoryResponse addEvaluation(@Valid ClinicalEvaluationRequest req) {
        User patient = users.findById(req.patientId())
                .filter(u -> u.getRol() == UserRol.Paciente)
                .orElseThrow(() -> new UserNotFoundException("Paciente no encontrado"));

        User doctor = users.findById(req.doctorId())
                .filter(u -> u.getRol() == UserRol.Odontologo)
                .orElseThrow(() -> new UserNotFoundException("Odontólogo no encontrado"));

        ClinicalHistory history = new ClinicalHistory();
        history.setPatient(patient);
        history.setDoctor(doctor);
        history.setMotivoConsulta(req.motivoConsulta().strip());
        history.setEnfermedadActual(req.enfermedadActual().strip());
        history.setBloodPressure(req.bloodPressure());
        history.setHeartRate(req.heartRate());
        history.setTemperature(req.temperature());
        history.setWeight(req.weight());
        history.setAtmArticulation(req.atmArticulation());
        history.setSoftTissues(req.softTissues());
        history.setOralHygiene(req.oralHygiene());
        history.setPlaqueIndex(req.plaqueIndex());
        history.setDentalCalculus(Boolean.TRUE.equals(req.dentalCalculus()));
        history.setFoodImpaction(Boolean.TRUE.equals(req.foodImpaction()));
        history.setPrimaryDiagnosis(req.primaryDiagnosis().strip());
        history.setSecondaryDiagnosis(req.secondaryDiagnosis());
        history.setPrognosis(req.prognosis());
        history.setSuggestedPlan(req.suggestedPlan());
        history.setDiagnosis(req.primaryDiagnosis().strip());
        history.setProcedureName("Evaluación / Examen Clínico");
        history.setToothPiece("General");
        history.setRecordDate(clock.instant());
        history.setSigned(true);

        historyRepo.save(history);
        return mapToResponse(history);
    }

    private ClinicalHistoryResponse mapToResponse(ClinicalHistory h) {
        String doctorName = h.getDoctor() != null ? "Dr. " + h.getDoctor().getLastName() : "No asignado";
        return new ClinicalHistoryResponse(
                h.getId(),
                h.getRecordDate(),
                h.getToothPiece(),
                h.getProcedureName(),
                h.getDiagnosis(),
                h.getObservations(),
                doctorName,
                h.isSigned()
        );
    }
}

