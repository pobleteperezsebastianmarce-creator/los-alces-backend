package com.alces.deportes.dto;

import com.alces.deportes.model.enums.EstadoMembresia;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class SocioResponseDTO {
    private Long id;
    private String rut;
    private String nombre;
    private String email;
    private String telefono;
    private String tipoMembresia;
    private EstadoMembresia estadoMembresia;
    private LocalDate fechaInscripcion;
}
