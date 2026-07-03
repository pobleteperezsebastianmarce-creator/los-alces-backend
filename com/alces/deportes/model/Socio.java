package com.alces.deportes.model;

import com.alces.deportes.model.enums.EstadoMembresia;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "socio")
public class Socio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 13)
    private String rut;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(length = 100)
    private String email;

    @Column(length = 20)
    private String telefono;

    @Column(name = "tipo_membresia", nullable = false, length = 50)
    private String tipoMembresia;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_membresia", nullable = false, length = 20)
    private EstadoMembresia estadoMembresia = EstadoMembresia.ACTIVA;

    @Column(name = "fecha_inscripcion", nullable = false)
    private LocalDate fechaInscripcion;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario gestor;

    @OneToMany(mappedBy = "socio", cascade = CascadeType.ALL, orphanRemoval = false)
    private List<PagoMensual> pagos = new ArrayList<>();
}
