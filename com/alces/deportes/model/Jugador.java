package com.alces.deportes.model;

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
@Table(name = "jugador")
public class Jugador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 13)
    private String rut;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false)
    private int edad;

    @Column(length = 20)
    private String estado = "ACTIVO";

    @ManyToOne(optional = false)
    @JoinColumn(name = "categoria_id", nullable = false)
    private CategoriaDeportiva categoria;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario administrador;

    @OneToMany(mappedBy = "jugador", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DocumentoJugador> documentos = new ArrayList<>();
}
