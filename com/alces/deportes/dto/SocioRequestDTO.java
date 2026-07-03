package com.alces.deportes.dto;

import com.alces.deportes.model.enums.EstadoMembresia;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class SocioRequestDTO {

    @NotBlank(message = "El RUT es obligatorio")
    @Size(min = 9, max = 13)
    private String rut;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @Email(message = "El email no es válido")
    private String email;

    private String telefono;

    @NotBlank(message = "El tipo de membresía es obligatorio")
    private String tipoMembresia;

    private EstadoMembresia estadoMembresia;

    @NotNull(message = "La fecha de inscripción es obligatoria")
    private LocalDate fechaInscripcion;
}
