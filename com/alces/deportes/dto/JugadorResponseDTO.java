package com.alces.deportes.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JugadorResponseDTO {
    private Long id;
    private String rut;
    private String nombre;
    private int edad;
    private String estado;
    private Long categoriaId;
    private String nombreCategoria;
}
