package co.edu.uniquindio.AgendasDT.repository;

import co.edu.uniquindio.AgendasDT.domain.MeetingAudit;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface MeetingAuditRepository extends JpaRepository<MeetingAudit, UUID> { }
