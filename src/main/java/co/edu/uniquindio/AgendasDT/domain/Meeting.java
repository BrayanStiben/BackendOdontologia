package co.edu.uniquindio.AgendasDT.domain;

import co.edu.uniquindio.AgendasDT.domain.enums.MeetingStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Table(name = "meeting")
@Entity
public class Meeting {

    //Atributos
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "ID")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "PatientID", nullable = false, updatable = false)
    private User patient;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "DoctorID", nullable = false)
    private User doctor;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "chair_id", nullable = false)
    private DentalChair chair;

    @Column(name = "Date", nullable = false)
    private Instant startsAt;

    @Column(name = "ends_at", nullable = false)
    private Instant endsAt;

    @Column(nullable = false, length = 500)
    private String reason;

    @Enumerated(EnumType.STRING)
    @Column(name = "Status", nullable = false)
    private MeetingStatus status;

    @Version
    @Column(nullable = false)
    private Long version;

    //Constructor vacio
    public Meeting() {

    }
}
