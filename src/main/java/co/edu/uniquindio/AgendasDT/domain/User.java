package co.edu.uniquindio.AgendasDT.domain;

import co.edu.uniquindio.AgendasDT.domain.enums.UserRol;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


import java.time.LocalDate;

@Getter
@Setter
@Table(name = "\"User\"")
@Entity
public class User {

    //Atributos
    @Id
    @Column(name = "ID")
    private Long id;
    @Column(name = "Name", nullable = false, length = 100)
    private String name;
    @Column(name = "LastName", nullable = false, length = 100)
    private String lastName;
    @Column(name = "BirthDate", nullable = false)
    private LocalDate birthDate;
    @Column(name = "Email", nullable = false, length = 254)
    private String email;
    @Column(name = "Phone", nullable = false, length = 20)
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(name = "Rol", nullable = false)
    private UserRol rol;

    @Version
    @Column(name = "Version", nullable = false)
    private Long version;

    @Column(name = "password")
    private String password;

    @Column(name = "tp", length = 50)
    private String tp;

    @Column(name = "clinic_name", length = 150)
    private String clinicName;

    @Column(name = "specialty", length = 100)
    private String specialty;

    @Column(name = "active", nullable = false)
    private boolean active = true;

    //Constructor vacio
    public User() {

    }
}
