package co.edu.uniquindio.AgendasDT.repository;

import co.edu.uniquindio.AgendasDT.domain.Meeting;
import co.edu.uniquindio.AgendasDT.domain.enums.MeetingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.Instant;
import java.util.Collection;
import java.util.UUID;

public interface MeetingRepository extends JpaRepository<Meeting, UUID> {
    @Query("""
            select count(m) > 0 from Meeting m
            where m.id <> :excludedId and m.status in :statuses
              and m.startsAt < :endsAt and m.endsAt > :startsAt
              and (m.patient.id = :patientId or m.doctor.id = :doctorId or m.chair.id = :chairId)
            """)
    boolean overlaps(@Param("excludedId") UUID excludedId, @Param("statuses") Collection<MeetingStatus> statuses,
                     @Param("startsAt") Instant startsAt, @Param("endsAt") Instant endsAt,
                     @Param("patientId") Long patientId, @Param("doctorId") Long doctorId,
                     @Param("chairId") Long chairId);

    long countByStartsAtBetween(Instant start, Instant end);

    long countByStartsAtBetweenAndStatus(Instant start, Instant end, MeetingStatus status);

    java.util.List<Meeting> findByStartsAtBetweenOrderByStartsAtAsc(Instant start, Instant end);

    java.util.List<Meeting> findByDoctorIdAndStartsAtBetween(Long doctorId, Instant start, Instant end);

    java.util.List<Meeting> findByPatientIdOrderByStartsAtDesc(Long patientId);
}
