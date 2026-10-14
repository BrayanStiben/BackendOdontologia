package co.edu.uniquindio.AgendasDT.repository;

import co.edu.uniquindio.AgendasDT.domain.UserAudit;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface UserAuditRepository extends JpaRepository<UserAudit, UUID> { }
