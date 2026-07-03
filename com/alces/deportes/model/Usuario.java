package com.alces.deportes.model;

import com.alces.deportes.model.enums.RolUsuario;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(name = "password_hash", nullable = false, length = 64)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private RolUsuario rol;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false)
    private boolean activo = true;

    @OneToMany(mappedBy = "administrador")
    private List<Jugador> jugadoresAdministrados = new ArrayList<>();

    @OneToMany(mappedBy = "gestor")
    private List<Socio> sociosGestionados = new ArrayList<>();
}
