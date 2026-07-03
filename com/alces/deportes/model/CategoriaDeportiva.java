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
@Table(name = "categoria_deportiva")
public class CategoriaDeportiva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre_categoria", nullable = false, length = 100)
    private String nombreCategoria;

    @Column(name = "edad_minima", nullable = false)
    private int edadMinima;

    @Column(name = "edad_maxima", nullable = false)
    private int edadMaxima;

    @OneToMany(mappedBy = "categoria")
    private List<Jugador> jugadores = new ArrayList<>();
}
