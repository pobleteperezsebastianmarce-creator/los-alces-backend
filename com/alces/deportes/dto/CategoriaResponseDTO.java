package com.alces.deportes.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CategoriaResponseDTO {
    private Long id;
    private String nombreCategoria;
    private int edadMinima;
    private int edadMaxima;
}
