package co.edu.uniquindio.AgendasDT.domain;

import co.edu.uniquindio.AgendasDT.domain.enums.UserRol;
import jakarta.persistence.*;
import lombok.Getter;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "user_audit")
@Getter
public class UserAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    @Column(name = "user_id", nullable = false, updatable = false)
    private Long userId;
    @Column(nullable = false, updatable = false)
    private String actor;
    @Column(nullable = false, updatable = false)
    private String action;
    @Column(name = "occurred_at", nullable = false, updatable = false)
    private Instant occurredAt;
    @Enumerated(EnumType.STRING)
    @Column(name = "previous_role", updatable = false)
    private UserRol previousRole;
    @Enumerated(EnumType.STRING)
    @Column(name = "new_role", nullable = false, updatable = false)
    private UserRol newRole;

    protected UserAudit() { }

    public UserAudit(Long userId, String actor, String action, UserRol previousRole, UserRol newRole) {
        this.userId = userId;
        this.actor = actor;
        this.action = action;
        this.occurredAt = Instant.now();
        this.previousRole = previousRole;
        this.newRole = newRole;
    }
}
