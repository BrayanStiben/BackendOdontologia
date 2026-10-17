package co.edu.uniquindio.AgendasDT.domain;

import co.edu.uniquindio.AgendasDT.domain.enums.NotificationStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Table(name = "notification")
@Entity
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meeting_id")
    private Meeting meeting;

    @Column(name = "type", nullable = false, length = 50)
    private String type;

    @Column(name = "recipient", nullable = false, length = 254)
    private String recipient;

    @Column(name = "message", length = 2000)
    private String message;

    @Column(name = "send_date", nullable = false)
    private Instant sendDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 50)
    private NotificationStatus status = NotificationStatus.Pendiente;

    public Notification() {
    }

    public Notification(Meeting meeting, String type, String recipient, String message, Instant sendDate, NotificationStatus status) {
        this.meeting = meeting;
        this.type = type;
        this.recipient = recipient;
        this.message = message;
        this.sendDate = sendDate;
        this.status = status;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public Meeting getMeeting() { return meeting; }
    public void setMeeting(Meeting meeting) { this.meeting = meeting; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getRecipient() { return recipient; }
    public void setRecipient(String recipient) { this.recipient = recipient; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public Instant getSendDate() { return sendDate; }
    public void setSendDate(Instant sendDate) { this.sendDate = sendDate; }

    public NotificationStatus getStatus() { return status; }
    public void setStatus(NotificationStatus status) { this.status = status; }
}
