package com.alces.deportes.dto;

import com.alces.deportes.model.enums.TipoMovimiento;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PagoMensualRequestDTO {

    @NotNull(message = "El socio es obligatorio")
    private Long socioId;

    @NotBlank(message = "El concepto es obligatorio")
    private String concepto;

    @NotNull(message = "El monto es obligatorio")
    @Min(value = 1, message = "El monto debe ser mayor a cero")
    private Double monto;

    @NotNull(message = "El tipo de movimiento es obligatorio")
    private TipoMovimiento tipoMovimiento;

    @NotNull(message = "El mes es obligatorio")
    @Min(1)
    private Integer mes;

    @NotNull(message = "El año es obligatorio")
    @Min(2020)
    private Integer anio;

    @NotNull(message = "La fecha de vencimiento es obligatoria")
    private LocalDate fechaVencimiento;
}
