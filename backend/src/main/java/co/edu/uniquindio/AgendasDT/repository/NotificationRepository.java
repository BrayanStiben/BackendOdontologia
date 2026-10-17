package co.edu.uniquindio.AgendasDT.repository;

import co.edu.uniquindio.AgendasDT.domain.Notification;
import co.edu.uniquindio.AgendasDT.domain.enums.NotificationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface NotificationRepository extends JpaRepository<Notification, UUID> {
    List<Notification> findByMeetingId(UUID meetingId);
    List<Notification> findByStatus(NotificationStatus status);
}

