package co.edu.uniquindio.AgendasDT.repository;

import co.edu.uniquindio.AgendasDT.domain.TDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface TDetailRepository extends JpaRepository<TDetail, UUID> {
    List<TDetail> findByTreatmentId(UUID treatmentId);
}

