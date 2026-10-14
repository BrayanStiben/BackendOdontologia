package co.edu.uniquindio.AgendasDT.repository;

import co.edu.uniquindio.AgendasDT.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query("select count(u) > 0 from User u where lower(trim(u.email)) = :email")
    boolean emailExists(@Param("email") String email);

    @Query("select count(u) > 0 from User u where lower(trim(u.email)) = :email and u.id <> :id")
    boolean emailExistsForAnotherUser(@Param("email") String email, @Param("id") Long id);
}
