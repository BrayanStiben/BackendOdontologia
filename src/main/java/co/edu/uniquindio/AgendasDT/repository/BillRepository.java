package co.edu.uniquindio.AgendasDT.repository;

import co.edu.uniquindio.AgendasDT.domain.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BillRepository extends JpaRepository<Bill, UUID> {
    List<Bill> findByPatientIdOrderByIssueDateDesc(Long patientId);
    Optional<Bill> findByBillNumber(String billNumber);

    @Query("select coalesce(sum(b.totalCost), 0.0) from Bill b where b.issueDate >= :start and b.issueDate < :end")
    Double sumTotalBetween(@Param("start") Instant start, @Param("end") Instant end);

    @Query("select count(b) from Bill b where b.issueDate >= :start and b.issueDate < :end")
    long countBetween(@Param("start") Instant start, @Param("end") Instant end);
}

