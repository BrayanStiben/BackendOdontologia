package co.edu.uniquindio.AgendasDT.service;

import co.edu.uniquindio.AgendasDT.domain.User;
import co.edu.uniquindio.AgendasDT.domain.UserAudit;
import co.edu.uniquindio.AgendasDT.domain.enums.UserRol;
import co.edu.uniquindio.AgendasDT.dto.*;
import co.edu.uniquindio.AgendasDT.exception.*;
import co.edu.uniquindio.AgendasDT.repository.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;
import java.util.Locale;
import java.util.Objects;

@Service
@Validated
@PreAuthorize("hasRole('Administrador')")
@Transactional
public class UserService {
    private final UserRepository users;
    private final UserAuditRepository audit;

    public UserService(UserRepository users, UserAuditRepository audit) {
        this.users = users;
        this.audit = audit;
    }

    public UserResponse create(@Valid UserCreateRequest request) {
        String email = normalizeEmail(request.email());
        if (users.existsById(request.id())) {
            throw new UserConflictException("Ya existe un usuario con esa identificación.");
        }
        if (users.emailExists(email)) {
            throw new UserConflictException("El correo ya está registrado.");
        }
        User user = new User();
        user.setId(request.id());
        apply(user, request.name(), request.lastName(), request.birthDate(), email, request.phone(), request.rol());
        // Null version makes Spring Data treat this assigned-ID entity as new (persist, not merge).
        users.saveAndFlush(user);
        audit.save(new UserAudit(user.getId(), actor(), "CREATE", null, user.getRol()));
        return UserResponse.from(user);
    }

    public UserResponse update(@Positive Long id, @Valid UserUpdateRequest request) {
        User user = find(id);
        if (!Objects.equals(user.getVersion(), request.version())) {
            throw new UserConflictException("El usuario cambió. Consulte sus datos antes de volver a editar.");
        }
        String email = normalizeEmail(request.email());
        if (users.emailExistsForAnotherUser(email, id)) {
            throw new UserConflictException("El correo ya está registrado.");
        }
        UserRol previousRole = user.getRol();
        apply(user, request.name(), request.lastName(), request.birthDate(), email, request.phone(), request.rol());
        users.flush();
        audit.save(new UserAudit(id, actor(), "UPDATE", previousRole, user.getRol()));
        return UserResponse.from(user);
    }

    @Transactional(readOnly = true)
    public UserResponse get(@Positive Long id) {
        return UserResponse.from(find(id));
    }

    private User find(Long id) {
        return users.findById(id).orElseThrow(UserNotFoundException::new);
    }

    private void apply(User user, String name, String lastName, LocalDate birthDate,
                       String email, String phone, UserRol rol) {
        user.setName(name.strip());
        user.setLastName(lastName.strip());
        user.setBirthDate(birthDate);
        user.setEmail(email);
        user.setPhone(phone);
        user.setRol(rol);
    }

    private String normalizeEmail(String email) {
        return email.strip().toLowerCase(Locale.ROOT);
    }

    private String actor() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
