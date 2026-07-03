package com.alces.deportes.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class JugadorRequestDTO {

    @NotBlank(message = "El RUT es obligatorio")
    @Size(min = 9, max = 13, message = "El RUT debe tener entre 9 y 13 caracteres")
    private String rut;

    @NotBlank(message = "El nombre del jugador es obligatorio")
    private String nombre;

    @NotNull(message = "La edad es obligatoria")
    @Min(value = 1, message = "La edad debe ser al menos 1")
    @Max(value = 99, message = "La edad no puede superar 99")
    private Integer edad;

    @Pattern(regexp = "ACTIVO|LESIONADO|SUSPENDIDO",
            message = "El estado debe ser ACTIVO, LESIONADO o SUSPENDIDO")
    private String estado;

    @NotNull(message = "El id de la categoría es obligatorio")
    private Long categoriaId;
}
