package com.alces.deportes.dto;

import com.alces.deportes.model.enums.EstadoMembresia;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class AlertaMorosidadDTO {
    private Long pagoId;
    private Long socioId;
    private String nombreSocio;
    private String rutSocio;
    private String concepto;
    private Double monto;
    private LocalDate fechaVencimiento;
    private int diasAtraso;
    private EstadoMembresia estadoMembresia;
}
