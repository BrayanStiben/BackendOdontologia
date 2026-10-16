package co.edu.uniquindio.AgendasDT.repository;

import co.edu.uniquindio.AgendasDT.domain.ClinicalHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface ClinicalHistoryRepository extends JpaRepository<ClinicalHistory, UUID> {
    List<ClinicalHistory> findByPatientIdOrderByRecordDateDesc(Long patientId);
}

