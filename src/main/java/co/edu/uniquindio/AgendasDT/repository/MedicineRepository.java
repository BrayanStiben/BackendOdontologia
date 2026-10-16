package co.edu.uniquindio.AgendasDT.repository;

import co.edu.uniquindio.AgendasDT.domain.Medicine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.UUID;

public interface MedicineRepository extends JpaRepository<Medicine, UUID> {
    @Query("select m from Medicine m where m.stock <= m.minimumStock")
    List<Medicine> findLowStock();

    @Query("select count(m) from Medicine m where m.stock <= m.minimumStock")
    long countLowStock();
}

