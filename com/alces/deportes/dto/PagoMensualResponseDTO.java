package com.alces.deportes.dto;

import com.alces.deportes.model.enums.EstadoPago;
import com.alces.deportes.model.enums.TipoMovimiento;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class PagoMensualResponseDTO {
    private Long id;
    private Long socioId;
    private String nombreSocio;
    private String concepto;
    private Double monto;
    private TipoMovimiento tipoMovimiento;
    private int mes;
    private int anio;
    private LocalDate fechaVencimiento;
    private LocalDate fechaPago;
    private EstadoPago estado;
}
