package co.edu.uniquindio.AgendasDT.repository;

import co.edu.uniquindio.AgendasDT.domain.BillDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface BillDetailRepository extends JpaRepository<BillDetail, UUID> {
    List<BillDetail> findByBillId(UUID billId);
}

