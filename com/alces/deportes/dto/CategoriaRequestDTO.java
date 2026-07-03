package com.alces.deportes.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CategoriaRequestDTO {

    @NotBlank(message = "El nombre de la categoría es obligatorio")
    private String nombreCategoria;

    @NotNull(message = "La edad mínima es obligatoria")
    @Min(value = 1, message = "La edad mínima debe ser al menos 1")
    @Max(value = 99, message = "La edad mínima no puede superar 99")
    private Integer edadMinima;

    @NotNull(message = "La edad máxima es obligatoria")
    @Min(value = 1, message = "La edad máxima debe ser al menos 1")
    @Max(value = 99, message = "La edad máxima no puede superar 99")
    private Integer edadMaxima;
}
